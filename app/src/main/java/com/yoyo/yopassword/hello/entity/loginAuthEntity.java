package com.yoyo.yopassword.hello.entity;

/**
 * Created by nidey on 2016/5/31.
 */
public class LoginAuthEntity {
    /*        "ret":0,
              "pay_token":"F2CB657CB0CB7EA18221663D6365835D",
              "pf":"desktop_m_qq-10000144-android-2002-",
              "query_authority_cost":817,
              "authority_cost":-161404158,
              "openid":"084B0B562F9713B22E55DEB060F2F7B2",
              "expires_in":7776000,
              "pfkey":"365794879923ba7a8ecb85136300e93b",
              "msg":"",
              "access_token":"45E8EABA87AED91D3325DF8067912CED",
              "login_cost":2998
  */
    long ret;
    String pay_token;
    String pf;
    long query_authority_cost;
    long authority_cost;
    String openid;
    long expires_in;
    String pfkey;
    String msg;
    String access_token;
    long login_cost;

    public long getRet() {
        return ret;
    }

    public void setRet(long ret) {
        this.ret = ret;
    }

    public String getPay_token() {
        return pay_token;
    }

    public void setPay_token(String pay_token) {
        this.pay_token = pay_token;
    }

    public String getPf() {
        return pf;
    }

    public void setPf(String pf) {
        this.pf = pf;
    }

    public long getQuery_authority_cost() {
        return query_authority_cost;
    }

    public void setQuery_authority_cost(long query_authority_cost) {
        this.query_authority_cost = query_authority_cost;
    }

    public long getAuthority_cost() {
        return authority_cost;
    }

    public void setAuthority_cost(long authority_cost) {
        this.authority_cost = authority_cost;
    }

    public String getOpenid() {
        return openid;
    }

    public void setOpenid(String openid) {
        this.openid = openid;
    }

    public long getExpires_in() {
        return expires_in;
    }

    public void setExpires_in(long expires_in) {
        this.expires_in = expires_in;
    }

    public String getPfkey() {
        return pfkey;
    }

    public void setPfkey(String pfkey) {
        this.pfkey = pfkey;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public String getAccess_token() {
        return access_token;
    }

    public void setAccess_token(String access_token) {
        this.access_token = access_token;
    }

    public long getLogin_cost() {
        return login_cost;
    }

    public void setLogin_cost(long login_cost) {
        this.login_cost = login_cost;
    }
}
