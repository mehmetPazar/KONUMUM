package com.example.mehmet.konumum;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.List;

public class OzelAdapter extends BaseAdapter {
    private LayoutInflater mInflater;
    private List<Kisiler> mKisiListesi;

    public OzelAdapter(Activity activity, List<Kisiler> kisiler) {
        mInflater = (LayoutInflater) activity.getSystemService(
                Context.LAYOUT_INFLATER_SERVICE);
        mKisiListesi = kisiler;
    }
    @Override
    public int getCount() {
        return mKisiListesi.size();
    }

    @Override
    public Kisiler getItem(int position) {
        return mKisiListesi.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View satirView;

        satirView = mInflater.inflate(R.layout.satir_layout, null);
        TextView isimsoyisim = satirView.findViewById(R.id.isimsoyisim);
        TextView mail=satirView.findViewById(R.id.mail);
        TextView tarih=satirView.findViewById(R.id.tarih);

        ImageView foto = satirView.findViewById(R.id.foto);

        Kisiler kisi = mKisiListesi.get(position);

        isimsoyisim.setText(kisi.getAdsoyad());
        mail.setText(kisi.getMail());
        tarih.setText(kisi.getTarih());
        Picasso.get().load(kisi.getResim()).into(foto);

        return satirView;
    }
}
