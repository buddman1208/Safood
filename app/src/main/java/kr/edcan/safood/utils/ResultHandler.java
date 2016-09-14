/*
 * Copyright (C) 2008 ZXing authors
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package kr.edcan.safood.utils;

import android.app.Activity;
import android.provider.ContactsContract;

import com.google.zxing.Result;
import com.google.zxing.client.result.ParsedResult;

/**
 * A base class for the Android-specific barcode handlers. These allow the app to polymorphically
 * suggest the appropriate actions for each data type.
 *
 * This class also contains a bunch of utility methods to take common actions like opening a URL.
 * They could easily be moved into a helper object, but it can't be static because the Activity
 * instance is needed to launch an intent.
 *
 * @author dswitkin@google.com (Daniel Switkin)
 * @author Sean Owen
 */
public abstract class ResultHandler {

  private static final String TAG = ResultHandler.class.getSimpleName();

  private static final String[] EMAIL_TYPE_STRINGS = {"home", "work", "mobile"};
  private static final String[] PHONE_TYPE_STRINGS = {"home", "work", "mobile", "fax", "pager", "main"};
  private static final String[] ADDRESS_TYPE_STRINGS = {"home", "work"};
  private static final int[] EMAIL_TYPE_VALUES = {
          ContactsContract.CommonDataKinds.Email.TYPE_HOME,
          ContactsContract.CommonDataKinds.Email.TYPE_WORK,
          ContactsContract.CommonDataKinds.Email.TYPE_MOBILE,
  };
  private static final int[] PHONE_TYPE_VALUES = {
          ContactsContract.CommonDataKinds.Phone.TYPE_HOME,
          ContactsContract.CommonDataKinds.Phone.TYPE_WORK,
          ContactsContract.CommonDataKinds.Phone.TYPE_MOBILE,
          ContactsContract.CommonDataKinds.Phone.TYPE_FAX_WORK,
          ContactsContract.CommonDataKinds.Phone.TYPE_PAGER,
          ContactsContract.CommonDataKinds.Phone.TYPE_MAIN,
  };
  private static final int[] ADDRESS_TYPE_VALUES = {
          ContactsContract.CommonDataKinds.StructuredPostal.TYPE_HOME,
          ContactsContract.CommonDataKinds.StructuredPostal.TYPE_WORK,
  };
  private static final int NO_TYPE = -1;

  public static final int MAX_BUTTON_COUNT = 4;

  private final ParsedResult result;
  private final Activity activity;
  private final Result rawResult;

  ResultHandler(Activity activity, ParsedResult result) {
    this(activity, result, null);
  }

  ResultHandler(Activity activity, ParsedResult result, Result rawResult) {
    this.result = result;
    this.activity = activity;
    this.rawResult = rawResult;
  }


  final Activity getActivity() {
    return activity;
  }

  /**
   * Indicates how many buttons the derived class wants shown.
   *
   * @return The integer button count.
   */
  public abstract int getButtonCount();

  /**
   * The text of the nth action button.
   *
   * @param index From 0 to getButtonCount() - 1
   * @return The button text as a resource ID
   */
  public abstract int getButtonText(int index);

  public Integer getDefaultButtonID() {
    return null;
  }

  /**
   * Execute the action which corresponds to the nth button.
   *
   * @param index The button that was clicked.
   */
  public abstract void handleButtonPress(int index);

  /**
   * Some barcode contents are considered secure, and should not be saved to history, copied to
   * the clipboard, or otherwise persisted.
   *
   * @return If true, do not create any permanent record of these contents.
   */
  public boolean areContentsSecure() {
    return false;
  }

  /**
   * Create a possibly styled string for the contents of the current barcode.
   *
   * @return The text to be displayed.
   */
  public CharSequence getDisplayContents() {
    String contents = result.getDisplayResult();
    return contents.replace("\r", "");
  }

}