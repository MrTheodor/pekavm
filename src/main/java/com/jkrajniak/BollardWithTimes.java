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

import java.util.StringJoiner;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.Vector;

public class BollardWithTimes extends Bollard {

  private final Vector mArrivalTimes; // Vector<ArrivalTime>

  public BollardWithTimes(JSONObject obj) throws JSONException {
    super(obj.getJSONObject("bollard"));
    JSONArray times = obj.getJSONArray("times");
    mArrivalTimes = new Vector(times.length());
    for (int i = 0; i < times.length(); ++i) {
      mArrivalTimes.addElement(new ArrivalTime(times.getJSONObject(i)));
    }
  }

  public Vector getArrivalTimes() {
    return mArrivalTimes;
  }

  public String toString() {
    StringBuilder sb = new StringBuilder(200);
    StringJoiner sj1 = new StringJoiner(":");
    sj1.add(getName());
    sj1.add(getSymbol());
    sj1.add(getTag());
    sb.append(sj1.toString());
    sb.append(" [");
    StringJoiner sj = new StringJoiner(";");
    mArrivalTimes.forEach(v->sj.add(((ArrivalTime)v).toString()));
    sb.append(sj.toString()).append("]");
    return sb.toString();
  }
}
