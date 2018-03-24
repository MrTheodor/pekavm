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

import org.json.JSONException;
import org.json.JSONObject;

public class Street {

  private final int mId;
  private final String mName;

  public Street(JSONObject obj) throws JSONException {
    mId = obj.getInt("id");
    mName = obj.getString("name");
  }

  public int getId() {
    return mId;
  }

  public String getName() {
    return mName;
  }

  public String toString() {
      return String.format("%s (%s)", mName, mId);
  }
}
