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

import java.util.Vector;

public class BollardWithDirections extends Bollard {

  private final Vector mDirections; // Vector<LineDirection>

  BollardWithDirections(JSONObject obj) throws JSONException {
    super(obj.getJSONObject("bollard"));
    JSONArray directions = obj.getJSONArray("directions");
    mDirections = new Vector(directions.length());
    for (int i = 0; i < directions.length(); ++i) {
      mDirections.addElement(new LineDirection(directions.getJSONObject(i)));
    }
  }

  public Vector getDirections() {
    return mDirections;
  }
}
