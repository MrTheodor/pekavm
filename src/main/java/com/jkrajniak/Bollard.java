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

public class Bollard {

  private final String mSymbol;
  private final String mTag;
  private final String mName;

  public Bollard(JSONObject obj) throws JSONException {
    mSymbol = obj.getString("symbol");
    mTag = obj.getString("tag");
    mName = obj.getString("name");
  }

  public String getSymbol() {
    return mSymbol;
  }

  public String getTag() {
    return mTag;
  }

  public String getName() {
    return mName;
  }

  public String toString() {
    return String.format("%s:%s:%s", mName, mSymbol, mTag);
  }

  public int hashCode() {
    int hash = 7;
    hash = 89 * hash + (this.mSymbol != null ? this.mSymbol.hashCode() : 0);
    hash = 89 * hash + (this.mTag != null ? this.mTag.hashCode() : 0);
    hash = 89 * hash + (this.mName != null ? this.mName.hashCode() : 0);
    return hash;
  }

  public boolean equals(Object obj) {
    if (obj == null) {
      return false;
    }
    if (getClass() != obj.getClass()) {
      return false;
    }
    final Bollard other = (Bollard) obj;
    if ((this.mSymbol == null) ? (other.mSymbol != null) : !this.mSymbol.equals(other.mSymbol)) {
      return false;
    }
    if ((this.mTag == null) ? (other.mTag != null) : !this.mTag.equals(other.mTag)) {
      return false;
    }
    if ((this.mName == null) ? (other.mName != null) : !this.mName.equals(other.mName)) {
      return false;
    }
    return true;
  }
}
