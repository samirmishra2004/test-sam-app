package com.share.trade.common;

import org.apache.commons.lang.StringUtils;
import org.apache.http.cookie.ClientCookie;
import org.apache.http.cookie.MalformedCookieException;
import org.apache.http.cookie.SetCookie;
import org.apache.http.impl.cookie.BasicExpiresHandler;
import org.apache.http.impl.cookie.BrowserCompatSpec;

class LenientCookieSpec extends BrowserCompatSpec {
    public LenientCookieSpec() {
        super();
        registerAttribHandler(ClientCookie.EXPIRES_ATTR, new BasicExpiresHandler(DATE_PATTERNS) {
            @Override public void parse(SetCookie cookie, String value) throws MalformedCookieException {
                if (StringUtils.isEmpty(value)) {
                    // You should set whatever you want in cookie
                    cookie.setExpiryDate(null);
                } else {
                    super.parse(cookie, value);
                }
            }
        });
    }
}