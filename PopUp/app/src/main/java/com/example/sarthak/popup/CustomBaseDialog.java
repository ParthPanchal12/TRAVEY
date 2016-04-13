package com.example.sarthak.popup;

import android.content.Context;
import android.graphics.Color;
import android.view.View;
import android.widget.TextView;

import com.flyco.animation.Attention.Swing;
import com.flyco.dialog.utils.CornerUtils;
import com.flyco.dialog.widget.base.BaseDialog;

/**
 * Created by sarthak on 13/4/16.
 */
public class CustomBaseDialog extends BaseDialog<CustomBaseDialog> {
    private TextView tv_cancel;
    private TextView tv_exit;
    private Context context;

    public CustomBaseDialog(Context context) {
        super(context);
        this.context=context;
    }

    @Override
    public View onCreateView() {
        widthScale(0.85f);
        showAnim(new Swing());

        // dismissAnim(this, new ZoomOutExit());
        View inflate = View.inflate(context, R.layout.pop, null);
        tv_cancel =(TextView) inflate.findViewById(R.id.tv);
        inflate.setBackgroundDrawable(
                CornerUtils.cornerDrawable(Color.parseColor("#ffffff"), dp2px(5)));

        return inflate;
    }


    @Override
    public void setUiBeforShow() {
        tv_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });

        return ;
    }
}