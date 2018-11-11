package io.github.ryanhoo.firFlight.ui.app;

import android.content.Context;
import android.graphics.Paint;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import butterknife.BindView;
import butterknife.ButterKnife;
import com.bumptech.glide.Glide;
import io.github.ryanhoo.firFlight.R;
import io.github.ryanhoo.firFlight.data.model.App;
import io.github.ryanhoo.firFlight.ui.common.adapter.IAdapterView;
import io.github.ryanhoo.firFlight.ui.common.widget.CharacterDrawable;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

/**
 * Created with Android Studio. User: ryan.hoo.j@gmail.com Date: 8/21/16 Time: 10:46 PM Desc:
 * AppItemView
 */

public class AppItemView extends RelativeLayout implements IAdapterView<App> {

    private static SimpleDateFormat FORMATTER
        = new SimpleDateFormat("yyyy.MM.dd", Locale.CHINA);

    Context mContext;
    @BindView(R.id.image_view_icon)
    ImageView imageView;
    @BindView(R.id.text_view_name)
    TextView textViewName;
    @BindView(R.id.text_view_local_version)
    TextView textViewLocalVersion;
    @BindView(R.id.text_view_version)
    TextView textViewVersion;
    @BindView(R.id.text_view_remote_date)
    TextView textViewRemoteDate;
    @BindView(R.id.text_view_bundle_id)
    TextView textViewBundleId;
    @BindView(R.id.button_action)
    Button buttonAction;
    @BindView(R.id.layout_ios_badge)
    View layoutIOSBadge;

    AppInfo appInfo;

    public AppItemView(Context context) {
        super(context);

        mContext = context;
        View.inflate(context, R.layout.item_app, this);
        ButterKnife.bind(this);

        textViewLocalVersion
            .setPaintFlags(textViewLocalVersion.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
    }

    @Override
    public void bind(App app, int position) {
        appInfo = new AppInfo(mContext, app);

        Glide.with(mContext)
            .load(app.getIconUrl())
            .placeholder(CharacterDrawable
                .create(mContext, app.getName().charAt(0), false, R.dimen.ff_padding_large))
            .into(imageView);
        textViewName.setText(app.getName());
        textViewVersion.setText(String.format("%s(%s)",
            app.getMasterRelease().getVersion(),
            app.getMasterRelease().getBuild()
        ));
        textViewBundleId.setText(app.getBundleId());

        // Hide old version when app is not installed or already up-to-date
        textViewLocalVersion.setVisibility(
            (appInfo.isUpToDate || !appInfo.isInstalled) ? View.GONE : View.VISIBLE);
        if (appInfo.isInstalled && !appInfo.isUpToDate) {
            textViewLocalVersion.setText(String.format("%s(%s)",
                appInfo.localVersionName, appInfo.localVersionCode));
        }
        boolean isAndroidApp = App.TYPE_ANDROID.equals(app.getType());
        buttonAction.setVisibility(isAndroidApp ? View.VISIBLE : View.GONE);
        layoutIOSBadge.setVisibility(isAndroidApp ? View.GONE : View.VISIBLE);

        //textViewLocalDate.setText(app.getCreatedAt().toString());
        Date date = app.getUpdatedAt();
        if (date != null) {
            textViewRemoteDate.setText(FORMATTER.format(date));
        }
    }
}
