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

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import com.jkrajniak.VMApi.BollardsWithTimesReceiver;
import java.io.IOException;
import java.util.Vector;
import org.json.JSONException;
import org.junit.jupiter.api.Test;

class VMApiTest {

  @Test
  void getStopPoints() {
    VMApi.getStopPoints("P", new VMApi.GetStopPointsReceiver() {
      @Override
      public void onStopPointsReceived(Vector<StopPoint> stopPoints) {
        assertEquals(11, stopPoints.size());
      }

      @Override
      public void onJSONError(JSONException e) {

      }

      @Override
      public void onCommError(IOException e) {

      }
    });
    VMApi.getStopPoints("Półwiejska", new VMApi.GetStopPointsReceiver() {
      @Override
      public void onStopPointsReceived(Vector<StopPoint> stopPoints) {
        assertEquals(1, stopPoints.size());
      }

      @Override
      public void onJSONError(JSONException e) {

      }

      @Override
      public void onCommError(IOException e) {

      }
    });
  }

  @Test
  void getBollardsByStopPoint() {
    VMApi.getBollardsByStopPoint("Półwiejska", new VMApi.BollardsByStopPointReceiver() {
      @Override
      public void onBollardsByStopPointReceived(
          Vector<BollardWithDirections> bollardsWithDirections) {
        assertEquals(bollardsWithDirections.size(), 4);
      }

      @Override
      public void onJSONError(JSONException e) {

      }

      @Override
      public void onCommError(IOException e) {

      }
    });
  }

  @Test
  void getBollardsByStreet() {
    VMApi.getBollardsByStreet("Półwiejska", new VMApi.BollardsByStreetReceiver() {
      @Override
      public void onBollardsByStreetReceived(Vector<BollardWithDirections> bollardsWithDirections) {
        assertEquals(4, bollardsWithDirections.size());
      }

      @Override
      public void onJSONError(JSONException e) {

      }

      @Override
      public void onCommError(IOException e) {

      }
    });
  }

  @Test
  void getBollardsByLine() {
    VMApi.getBollardsByLine("71", new VMApi.BollardsByLineReceiver() {
      @Override
      public void onBollardsByLineReceived(Vector<? extends LineRoute> lineRoutes) {
        assertEquals(2, lineRoutes.size());
      }

      @Override
      public void onJSONError(JSONException e) {

      }

      @Override
      public void onCommError(IOException e) {

      }
    });
  }

  @Test
  void getLines() {
    VMApi.getLines("71", new VMApi.LinesReceiver() {
      @Override
      public void onLinesReceived(Vector<? extends String> lineNames) {
        assertEquals(3, lineNames.size());
      }

      @Override
      public void onJSONError(JSONException e) {

      }

      @Override
      public void onCommError(IOException e) {

      }
    });
  }

  @Test
  void getStreets() {
    VMApi.getStreets("al. Niepodległości", new VMApi.StreetsReceiver() {
      @Override
      public void onStreetsReceived(Vector<Street> streets) {
        assertEquals(1, streets.size());
      }

      @Override
      public void onJSONError(JSONException e) {

      }

      @Override
      public void onCommError(IOException e) {

      }
    });
  }

  @Test
  void getTimes() {
    VMApi.getTimes("OKOS01", new VMApi.TimesReceiver() {
      @Override
      public void onTimesReceived(BollardWithTimes times) {
        assertEquals("OKOS01", times.getSymbol());
      }

      @Override
      public void onJSONError(JSONException e) {
      }

      @Override
      public void onCommError(IOException e) {
      }
    });
  }

  @Test
  void getTimesWrongName() {
    Throwable exception = assertThrows(IllegalArgumentException.class, () -> {
      VMApi.getTimes("OCZ", new VMApi.TimesReceiver() {
        @Override
        public void onTimesReceived(BollardWithTimes times) {
        }

        @Override
        public void onJSONError(JSONException e) {
        }

        @Override
        public void onCommError(IOException e) {
        }
      });
    });
    assertEquals("brak", exception.getMessage());
  }

  @Test
  void getTimesForAllBollards() {
    VMApi.getTimesForAllBollards("Libelta",
        new BollardsWithTimesReceiver() {
          @Override
          public void onBollardsWithTimesReceived(
              Vector<? extends BollardWithTimes> bollardsWithTimes) {
            assertEquals(1, bollardsWithTimes.size());
          }

          @Override
          public void onJSONError(JSONException e) {
          }

          @Override
          public void onCommError(IOException e) {

          }
        });
  }
}