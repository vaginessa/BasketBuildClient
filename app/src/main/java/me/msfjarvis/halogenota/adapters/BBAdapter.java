package me.msfjarvis.halogenota.adapters;

import android.content.Context;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.customtabs.CustomTabsIntent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.afollestad.materialdialogs.DialogAction;
import com.afollestad.materialdialogs.MaterialDialog;

import java.util.List;

import me.msfjarvis.halogenota.R;
import me.msfjarvis.halogenota.data.BasketBuildFile;


public class BBAdapter extends ArrayAdapter<BasketBuildFile> {
    public Context context;

    public BBAdapter(Context context, int resource, List<BasketBuildFile> items) {
        super(context, resource, items);
        this.context = context;
    }

    private void customTab(String Url) {
        CustomTabsIntent.Builder builder = new CustomTabsIntent.Builder();
        builder.setShowTitle(true);
        CustomTabsIntent customTabsIntent = builder.build();
        customTabsIntent.launchUrl(context, Uri.parse(Url));
    }

    @Override
    public View getView(int position, final View convertView, ViewGroup parent) {
        View v = convertView;
        if (v == null)
            v = LayoutInflater.from(getContext()).inflate(R.layout.bb_file, null);
        final BasketBuildFile p = getItem(position);
        if (p != null) {
            TextView name = (TextView) v.findViewById(R.id.textView);
            name.setText(p.filename);
            v.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View view) {
                    customTab(p.url);
                    return false;
                }
            });
            v.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    new MaterialDialog.Builder(context)
                            .positiveText("Download")
                            .neutralText("OK")
                            .content(String.format(context.getString(R.string.dialog_content), p.filename, p.file_size,p.hDate))
                            .onPositive(new MaterialDialog.SingleButtonCallback() {
                                @Override
                                public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                                    dialog.dismiss();
                                    customTab(p.url);
                                }
                            })
                            .onNeutral(new MaterialDialog.SingleButtonCallback() {
                                @Override
                                public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                                    dialog.dismiss();
                                }
                            })
                            .show();
                }
            });
        }
        return v;
    }
}

