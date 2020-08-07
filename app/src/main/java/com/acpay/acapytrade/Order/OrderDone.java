package com.acpay.acapytrade.Order;

import android.os.Handler;
import android.util.Log;

import com.acpay.acapytrade.Networking.JasonReponser;

public class OrderDone {

    String response;
    private static boolean finish = false;

    public static boolean isFinish() {
        return finish;
    }

    public void setFinish(boolean finish) {
        this.finish = finish;
    }

    public OrderDone(Order order, String methode) {
        switch (methode) {
            case "enable":
                Enable(order);
                break;
            case "delete":
                Delete(order);
                break;
            case "pending":
                Pending(order);
                break;
            case "done":
                complete(order);
                break;
        }

    }

    public OrderDone(Order order, String methode, String value) {
        updateNotes(order, value);

    }

    public void Enable(Order order) {
        finish = false;
        String api = "https://www.app.acapy-trade.com/enableOrder.php?order=" + order.getOrderNum();
        Log.w("pen", api);
        final JasonReponser update = new JasonReponser();
        update.setFinish(false);
        update.execute(api);
        final Handler handler = new Handler();
        Runnable runnableCode = new Runnable() {
            @Override
            public void run() {
                if (update.isFinish()) {
                    response = update.getUserId();
                    finish = true;
                } else {
                    handler.postDelayed(this, 100);
                }
            }
        };
        handler.post(runnableCode);
    }

    public void Delete(Order order) {
        finish = false;
        String api = "https://www.app.acapy-trade.com/deleteOrder.php?order=" + order.getOrderNum();
        Log.w("pen", api);
        final JasonReponser update = new JasonReponser();
        update.setFinish(false);
        update.execute(api);
        final Handler handler = new Handler();
        Runnable runnableCode = new Runnable() {
            @Override
            public void run() {
                if (update.isFinish()) {
                    response = update.getUserId();
                    finish = true;
                } else {
                    handler.postDelayed(this, 100);
                }
            }
        };
        handler.post(runnableCode);
    }

    public void Pending(Order order) {
        finish = false;
        String api = "https://www.app.acapy-trade.com/pendingOrder.php?order=" + order.getOrderNum();
        Log.w("pen", api);
        final JasonReponser update = new JasonReponser();
        update.setFinish(false);
        update.execute(api);
        final Handler handler = new Handler();
        Runnable runnableCode = new Runnable() {
            @Override
            public void run() {
                if (update.isFinish()) {
                    response = update.getUserId();
                    finish = true;
                } else {
                    handler.postDelayed(this, 100);
                }
            }
        };
        handler.post(runnableCode);
    }

    public void complete(Order order) {
        String api = "https://www.app.acapy-trade.com/doneOrder.php?order=" + order.getOrderNum();
        Log.w("pen", api);
        final JasonReponser update = new JasonReponser();
        update.setFinish(false);
        update.execute(api);
        final Handler handler = new Handler();
        Runnable runnableCode = new Runnable() {
            @Override
            public void run() {
                if (update.isFinish()) {
                    response = update.getUserId();
                    finish = true;
                } else {
                    handler.postDelayed(this, 100);
                }
            }
        };
        handler.post(runnableCode);
    }

    private void updateNotes(Order order, String value) {
        finish = false;
        String api = "https://www.app.acapy-trade.com/updatenotes.php?order=" + order.getOrderNum() + "&note=" + value;
        Log.w("pen", api);
        final JasonReponser update = new JasonReponser();
        update.setFinish(false);
        update.execute(api);
        final Handler handler = new Handler();
        Runnable runnableCode = new Runnable() {
            @Override
            public void run() {
                if (update.isFinish()) {
                    response = update.getUserId();
                    finish = true;
                } else {
                    handler.postDelayed(this, 100);
                }
            }
        };
        handler.post(runnableCode);
    }

    public String getResponse() {
        return response;
    }
}
