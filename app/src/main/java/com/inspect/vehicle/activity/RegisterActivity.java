package com.inspect.vehicle.activity;

import android.support.v7.widget.PopupMenu;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import com.inspect.vehicle.R;
import com.inspect.vehicle.base.BaseActivity;

import butterknife.OnClick;

public class RegisterActivity extends BaseActivity implements PopupMenu.OnMenuItemClickListener {

    @Override
    protected int actLayoutRes() {
        return R.layout.activity_register;
    }

    @Override
    public void afterOnCreate() {
        setTitle(R.string.action_register);
    }

    @OnClick({R.id.btn_register})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btn_register:
               /* builder.putExtra("account", "111111");
                builder.putExtra("password", "111111");
                setResult(RESULT_OK, builder);
                finish();*/
               showMenu(view);
                break;
        }
    }

    private void showMenu(View showView) {
        PopupMenu popup = new PopupMenu(this, showView);//第二个参数是绑定的那个view
        //填充菜单
        popup.inflate(R.menu.menu_deal);
        //绑定菜单项的点击事件
        popup.setOnMenuItemClickListener(this);
        popup.setGravity(Gravity.END);
        popup.show(); //这一行代码不要忘记了
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.refresh, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.refresh:
                showToast("刷  新");
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onMenuItemClick(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.item_this:
                System.out.println("继续使用是身份证");
                return true;
            case R.id.item_other:
                System.out.println("使用其他身份证");
                return true;
            case R.id.item_home:
                System.out.println("处理其他车辆");
                return true;
        }
        return false;
    }
}
