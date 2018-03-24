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

import org.json.JSONObject;
import org.json.JSONException;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class ArrivalTime {

  private final boolean mRealTime;
  private final int mMinutesToArrive;
  private final String mDestination;
  private final boolean mAtStopPoint;
  private final LocalDateTime mDepartureTime;
  private final String mLine;

  ArrivalTime(JSONObject obj) throws JSONException {
    mRealTime = obj.getBoolean("realTime");
    mMinutesToArrive = obj.getInt("minutes");
    mDestination = obj.getString("direction");
    mAtStopPoint = obj.getBoolean("onStopPoint");
    mLine = obj.getString("line");
    DateTimeFormatter dateTimeFormatter = DateTimeFormatter
        .ofPattern("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'");
    mDepartureTime = LocalDateTime.parse(obj.getString("departure"), dateTimeFormatter);
  }

  public boolean isRealTime() {
    return mRealTime;
  }

  public int getMinutesToArrive() {
    return mMinutesToArrive;
  }

  public String getDestination() {
    return mDestination;
  }

  public boolean isAtStopPoint() {
    return mAtStopPoint;
  }

  public LocalDateTime getDepartureTime() {
    return mDepartureTime;
  }

  public String getLine() {
    return mLine;
  }

  public String toString() {
    StringBuilder sb = new StringBuilder(200);
    sb.append(mLine).append(":");
    sb.append(mDestination);
    sb.append(" left: ").append(mMinutesToArrive);
    sb.append(" atStop: ").append(mAtStopPoint);
    sb.append(" departure: ").append(mDepartureTime.toString());
    return sb.toString();
  }
}
