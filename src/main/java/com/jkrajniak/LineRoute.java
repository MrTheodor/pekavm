/*
 pekavm - PEKA Virtual Monitor API library

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

import java.util.StringJoiner;
import java.util.Vector;

public class LineRoute {

  private final LineDirection mDirection;
  private final Vector mBollards; // Vector<Bollard>

  LineRoute(JSONObject obj) throws JSONException {
    mDirection = new LineDirection(obj.getJSONObject("direction"));
    JSONArray bollards = obj.getJSONArray("bollards");
    mBollards = new Vector<Bollard>(bollards.length());
    for (int i = 0; i < bollards.length(); ++i) {
      mBollards.addElement(new Bollard(bollards.getJSONObject(i)));
    }
  }

  public LineDirection getDirection() {
    return mDirection;
  }

  public Vector getBollards() {
    return mBollards;
  }

  public String toString() {
    StringBuilder sb = new StringBuilder(200);
    sb.append("Line: ").append(mDirection.getLine());
    sb.append(" (").append(mDirection.getDestination()).append(") ");
    StringJoiner sj = new StringJoiner("-");

    mBollards.forEach(((v)->sj.add(((Bollard)v).toString())));
    sb.append(" [").append(sj.toString()).append("]");
    return sb.toString();
  }
}
