/*
 * Copyright 2013 JNRain
 * 
 * Licensed under the Apache License, Version 2.0 (the "License"); you may
 * not use this file except in compliance with the License. You may obtain a
 * copy of the License at
 * 
 * http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the
 * License for the specific language governing permissions and limitations
 * under the License.
 */
package org.jnrain.mobile.accounts.kbs;

import java.io.ByteArrayInputStream;
import java.io.IOException;

import name.xen0n.cytosol.data.SimpleReturnCode;

import org.codehaus.jackson.annotate.JsonIgnore;
import org.codehaus.jackson.annotate.JsonIgnoreProperties;

import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.util.Base64;


@JsonIgnoreProperties(ignoreUnknown = true)
public class KBSRegisterResult extends SimpleReturnCode {
    private String captcha;

    public String getCaptcha() {
        return captcha;
    }

    public void setCaptcha(String captcha) {
        this.captcha = captcha;
    }

    @JsonIgnore
    public Drawable getCaptchaDrawable() {
        if (captcha != null) {
            byte[] captchaImgData = Base64.decode(captcha, Base64.DEFAULT);
            ByteArrayInputStream captchaStream = new ByteArrayInputStream(
                    captchaImgData);

            try {
                return BitmapDrawable.createFromStream(captchaStream, "src");
            } finally {
                try {
                    captchaStream.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }

        return null;
    }
}
