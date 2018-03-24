/*
 pekavm

 Copyright (C) 2018  Jakub Krajniak (jkrajniak at gmail.com)

 This program is free software: you can redistribute it and/or modify
 it under the terms of the GNU General Public License as published by
 the Free Software Foundation, either version 3 of the License, or
 (at your option) any later version.

 This program is distributed in the hope that it will be useful,
 but WITHOUT ANY WARRANTY; without even the implied warranty of
 MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 GNU General Public License for more details.

 You should have received a copy of the GNU General Public License
 along with this program.  If not, see <http://www.gnu.org/licenses/>.

 This code is strongly based on the work of: https://github.com/xavery/PekaVMME
 */

package com.jkrajniak;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Vector;
import java.util.logging.Level;
import java.util.logging.Logger;

public class VMApi {

  private static final Logger logger = Logger.getLogger( VMApi.class.getName() );

  public interface ResultReceiver {

    void onJSONError(JSONException e);

    void onCommError(IOException e);
  }

  public interface GetStopPointsReceiver extends ResultReceiver {

    void onStopPointsReceived(final Vector<StopPoint> stopPoints);
  }

  public interface BollardsByStopPointReceiver extends ResultReceiver {

    void onBollardsByStopPointReceived(final Vector<BollardWithDirections> bollardsWithDirections);
  }

  public interface BollardsByStreetReceiver extends ResultReceiver {

    void onBollardsByStreetReceived(final Vector<BollardWithDirections> bollardsWithDirections);
  }

  public interface BollardsByLineReceiver extends ResultReceiver {

    void onBollardsByLineReceived(final Vector<? extends LineRoute> lineRoutes);
  }

  public interface LinesReceiver extends ResultReceiver {

    void onLinesReceived(final Vector<? extends String> lineNames);
  }

  public interface StreetsReceiver extends ResultReceiver {

    void onStreetsReceived(final Vector<Street> streets);
  }

  public interface TimesReceiver extends ResultReceiver {

    void onTimesReceived(final BollardWithTimes times);
  }

  public interface BollardsWithTimesReceiver extends ResultReceiver {

    void onBollardsWithTimesReceived(final Vector<? extends BollardWithTimes> bollardsWithTimes);
  }

  private static HttpURLConnection createConnection() throws IOException {
    URL urlPEKA = new URL("https://www.peka.poznan.pl/vm/method.vm?ts=" + System.currentTimeMillis());
    HttpURLConnection conn = (HttpURLConnection) urlPEKA.openConnection();
    conn.setRequestMethod("POST");
    conn.setDoOutput(true);
    conn.setRequestProperty("User-Agent", "com.jkrajniak.pekavm/1.0");
    conn.setRequestProperty("Content-Type", "application/x-www-form-urlencoded; charset=UTF-8");
    return conn;
  }

  private static class Invocation {

    Invocation(final String m, final String a) {
      methodName = m;
      arguments = a;
    }
    final String methodName;
    final String arguments;
  }

  private static void addMethodParams(OutputStream connOutputStream, Invocation params)
          throws IOException {
    try {
      String postParams = "method=" + params.methodName + "&p0=" + params.arguments;
      byte[] encodedParams = postParams.getBytes();
      connOutputStream.write(encodedParams);
    } catch (UnsupportedEncodingException ex) {
      logger.log(Level.SEVERE, "addMethodParams exception: ", ex);
    }
  }

  private interface MethodContext {
    Invocation getParams();
    void parse(JSONObject o) throws JSONException;
  }

  private static void callAPI(ResultReceiver rcv, MethodContext m) {
    HttpURLConnection conn = null;
    OutputStream out = null;
    InputStream in = null;
    IOException ioEx = null;
    JSONException jsonEx = null;
    try {
      conn = createConnection();
      out = conn.getOutputStream();
      addMethodParams(out, m.getParams());
      in = conn.getInputStream();
      JSONObject o = getJSONFromInputStream(in);
      try {
        String failure = o.getString("failure");
        throw new IllegalArgumentException(failure);
      } catch (JSONException ignored) {
        m.parse(o);
      }
    } catch (IOException ex) {
      ioEx = ex;
    } catch (JSONException ex) {
      jsonEx = ex;
    } finally {
      try {
        // no option but to ignore these.
        if (out != null) {
          out.close();
        }
        if (in != null) {
          in.close();
        }
        if (conn != null) {
          conn.disconnect();
        }
      } catch (IOException e) {
        logger.log(Level.SEVERE, "callAPI", e);
      }
    }
    if (ioEx != null) {
      rcv.onCommError(ioEx);
    }
    if (jsonEx != null) {
      rcv.onJSONError(jsonEx);
    }
  }

  public static void getStopPoints(final String pattern, final GetStopPointsReceiver cbk) {
    callAPI(cbk, new MethodContext() {
      public Invocation getParams() {
        try {
          JSONObject o = new JSONObject();
          o.put("pattern", pattern);
          return new Invocation("getStopPoints", o.toString());
        } catch (JSONException e) {
          logger.log(Level.WARNING, "getStopPoints", e);
        }
        return null;
      }

      public void parse(JSONObject o) throws JSONException {
        JSONArray result = o.getJSONArray("success");
        Vector<StopPoint> stopPoints = new Vector<StopPoint>(result.length());
        for (int i = 0; i < result.length(); ++i) {
          stopPoints.addElement(new StopPoint(result.getJSONObject(i)));
        }
        cbk.onStopPointsReceived(stopPoints);
      }
    });
  }

  public static void getBollardsByStopPoint(final String name,
          final BollardsByStopPointReceiver cbk) {
    callAPI(cbk, new MethodContext() {
      public Invocation getParams() {
        try {
          JSONObject o = new JSONObject();
          o.put("name", name);
          return new Invocation("getBollardsByStopPoint", o.toString());
        } catch (JSONException e) {
          logger.log(Level.INFO, "getBollardsByStopPoint", e);
        }
        return null;
      }

      public void parse(JSONObject o) throws JSONException {
        JSONArray bollards = o.getJSONObject("success").getJSONArray("bollards");
        Vector<BollardWithDirections> bollardsWithDirections = new Vector<BollardWithDirections>(bollards.length());
        for (int i = 0; i < bollards.length(); ++i) {
          bollardsWithDirections.addElement(new BollardWithDirections(bollards.getJSONObject(i)));
        }
        cbk.onBollardsByStopPointReceived(bollardsWithDirections);
      }
    });
  }

  public static void getBollardsByStreet(final String name, final BollardsByStreetReceiver cbk) {
    callAPI(cbk, new MethodContext() {
      public Invocation getParams() {
        try {
          JSONObject o = new JSONObject();
          o.put("name", name);
          return new Invocation("getBollardsByStreet", o.toString());
        } catch (JSONException e) {
          logger.log(Level.INFO, "getBollardsByStreet", e);
        }
        return null;
      }

      public void parse(JSONObject o) throws JSONException {
        JSONArray bollards = o.getJSONObject("success").getJSONArray("bollards");
        Vector<BollardWithDirections> bollardsWithDirections = new Vector<BollardWithDirections>(bollards.length());
        for (int i = 0; i < bollards.length(); ++i) {
          bollardsWithDirections.addElement(new BollardWithDirections(bollards.getJSONObject(i)));
        }
        cbk.onBollardsByStreetReceived(bollardsWithDirections);
      }
    });
  }

  public static void getBollardsByLine(final String name, final BollardsByLineReceiver cbk) {
    callAPI(cbk, new MethodContext() {
      public Invocation getParams() {
        try {
          JSONObject o = new JSONObject();
          o.put("name", name);
          return new Invocation("getBollardsByLine", o.toString());
        } catch (JSONException e) {
          logger.log(Level.INFO, "getBollardsByLine", e);
        }
        return null;
      }

      public void parse(JSONObject o) throws JSONException {
        JSONArray directions = o.getJSONObject("success").getJSONArray("directions");
        Vector<LineRoute> lineRoutes = new Vector<>(directions.length());
        for (int i = 0; i < directions.length(); ++i) {
          lineRoutes.addElement(new LineRoute(directions.getJSONObject(i)));
        }
        cbk.onBollardsByLineReceived(lineRoutes);
      }
    });
  }

  public static void getLines(final String pattern, final LinesReceiver cbk) {
    callAPI(cbk, new MethodContext() {
      public Invocation getParams() {
        try {
          JSONObject o = new JSONObject();
          o.put("pattern", pattern);
          return new Invocation("getLines", o.toString());
        } catch (JSONException e) {
          logger.log(Level.INFO, "getLines", e);
        }
        return null;
      }

      public void parse(JSONObject o) throws JSONException {
        JSONArray names = o.getJSONArray("success");
        Vector<String> lineNames = new Vector<>(names.length());
        for (int i = 0; i < names.length(); ++i) {
          lineNames.addElement(names.getJSONObject(i).getString("name"));
        }
        cbk.onLinesReceived(lineNames);
      }
    });
  }

  public static void getStreets(final String pattern, final StreetsReceiver cbk) {
    callAPI(cbk, new MethodContext() {
      public Invocation getParams() {
        try {
          JSONObject o = new JSONObject();
          o.put("pattern", pattern);
          return new Invocation("getStreets", o.toString());
        } catch (JSONException e) {
          logger.log(Level.INFO, "getStreets", e);
        }
        return null;
      }

      public void parse(JSONObject o) throws JSONException {
        JSONArray streetsArray = o.getJSONArray("success");
        Vector<Street> streets = new Vector<>(streetsArray.length());
        for (int i = 0; i < streetsArray.length(); ++i) {
          streets.addElement(new Street(streetsArray.getJSONObject(i)));
        }
        cbk.onStreetsReceived(streets);
      }
    });
  }

  public static void getTimes(final String bollardTag, final TimesReceiver cbk) {
    callAPI(cbk, new MethodContext() {
      public Invocation getParams() {
        try {
          JSONObject o = new JSONObject();
          o.put("symbol", bollardTag);
          return new Invocation("getTimes", o.toString());
        } catch (JSONException e) {
          logger.log(Level.INFO, "getTimes", e);
        }
        return null;
      }

      public void parse(JSONObject o) throws JSONException {
        try {
          cbk.onTimesReceived(new BollardWithTimes(o.getJSONObject("success")));
        } catch (JSONException e) {
          System.out.println(e);
        }
      }
    });
  }

  public static void getTimesForAllBollards(final String stopPointName,
          final BollardsWithTimesReceiver cbk) {
    callAPI(cbk, new MethodContext() {
      public Invocation getParams() {
        try {
          JSONObject o = new JSONObject();
          o.put("name", stopPointName);
          return new Invocation("getTimesForAllBollards", o.toString());
        } catch (JSONException e) {
          logger.log(Level.INFO, "getTimesForAllBollards", e);
        }
        return null;
      }

      public void parse(JSONObject o) throws JSONException {
        JSONArray array = o.getJSONObject("success").getJSONArray("bollardsWithTimes");
        Vector<BollardWithTimes> bollardsWithTimes = new Vector<BollardWithTimes>(array.length());
        for (int i = 0; i < array.length(); ++i) {
          bollardsWithTimes.addElement(new BollardWithTimes(array.getJSONObject(i)));
        }
        cbk.onBollardsWithTimesReceived(bollardsWithTimes);
      }
    });
  }

  private static JSONObject getJSONFromInputStream(InputStream in)
          throws IOException, JSONException {

    byte[] buf = new byte[4096]; // hopefully enough for one go...
    int readBytes;
    int offset = 0;
    while ((readBytes = in.read(buf, offset, buf.length - offset)) != -1) {
      offset += readBytes;
      if (offset == buf.length) {
        byte[] newbuf = new byte[buf.length * 2];
        System.arraycopy(buf, 0, newbuf, 0, buf.length);
        buf = newbuf;
      }
    }
    return new JSONObject(new String(buf, 0, offset));
  }
}
