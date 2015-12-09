package ip.cynic.mobilesafe.domain;

/**
 * Created by Administrator on 2015/12/8.
 */
public class BlackNumber {

    private String number;
    private String mode;

    public String getNumber() {
        return number;
    }

    public void setNumber(String number) {
        this.number = number;
    }

    public String getMode() {
        return mode;
    }

    public void setMode(String mode) {
        if ("1".equals(mode)) {
            this.mode = "短信+电话拦截";
        } else if ("2".equals(mode)) {
            this.mode = "短信拦截";
        } else if ("3".equals(mode)) {
            this.mode = "电话拦截";
        } else {
            this.mode = mode;
        }

    }

    public BlackNumber(String number, String mode) {
        this.number = number;
        setMode(mode);
    }
}
