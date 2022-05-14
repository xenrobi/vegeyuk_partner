package com.example.vegeyuk.restopatner.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.vegeyuk.restopatner.R;
import com.example.vegeyuk.restopatner.activities.resto.AddMenuActivity;
import com.example.vegeyuk.restopatner.adapter.MenuAdapter;
import com.example.vegeyuk.restopatner.models.Kategori;
import com.example.vegeyuk.restopatner.models.Menu;

import java.util.ArrayList;
import java.util.List;

public class MenuListFragment extends Fragment {

    private List<Menu> menuList = new ArrayList<>();
    private List<Kategori> kategoriList = new ArrayList<>();
    private List<Menu> menuListTemp = new ArrayList<>();
    private RecyclerView recyclerView;
    private MenuAdapter adapter;
   // MenuAdapter.ClickListener listener;
    int position;


    View message;
    ImageView icon_message;
    TextView title_message,sub_title_message;
    Button btnTmbh,mAddMenu;



    Kategori kategori[];
    public static MenuListFragment newInstance()
    {
        return new MenuListFragment();
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_menu_list,container,false);
        message = view.findViewById(R.id.error);
        icon_message = (ImageView) view.findViewById(R.id.img_msg);
        title_message =  (TextView) view.findViewById(R.id.title_msg);
        sub_title_message =  (TextView) view.findViewById(R.id.sub_title_msg);
        btnTmbh = (Button) view.findViewById(R.id.btnAdd);
        mAddMenu = view.findViewById(R.id.btnAddMenu);

        recyclerView =(RecyclerView) view.findViewById(R.id.my_recycler_view);

        initviews();
        return view;
    }

    private void initviews() {

        menuList = (List<Menu>) getArguments().getSerializable("menu");
        kategoriList = (List<Kategori>) getArguments().getSerializable("kategori");
        position = getArguments().getInt("position");

      //  listener = this;
        kategori = new Kategori[kategoriList.size()];
        setByKategori();


        mAddMenu.setText(String.valueOf("+ "+kategori[getArguments().getInt("position")].getKategoriNama().toString()));

        if(menuListTemp.size() >0 ) {
            adapter = new MenuAdapter(getActivity(), menuListTemp);
            RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getContext());
            recyclerView.setLayoutManager(layoutManager);
            recyclerView.setItemAnimator(new DefaultItemAnimator());
            recyclerView.setAdapter(adapter);
        }else {
            btnTmbh.setVisibility(View.VISIBLE);
            btnTmbh.setText("Tambah "+kategori[getArguments().getInt("position")].getKategoriNama().toString());
            btnTmbh.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    toAdd();
                }
            });
            mAddMenu.setVisibility(View.GONE);
            message.setVisibility(View.VISIBLE);
            icon_message.setImageResource(R.drawable.msg_menu);
            title_message.setText("Ayo Tambah Menu "+kategori[getArguments().getInt("position")].getKategoriNama().toString());
            sub_title_message.setVisibility(View.GONE);
        }



        mAddMenu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
               toAdd();
            }
        });

    }

    private void toAdd() {
        Toast.makeText(getActivity(),"Kode "+kategori[position].getId().toString(),Toast.LENGTH_SHORT).show();
        Intent intent = new Intent(getActivity(), AddMenuActivity.class);
        intent.putExtra("id_kategori",kategori[position].getId().toString());
        intent.putExtra("nama_kategori",kategori[position].getKategoriNama().toString());
        startActivity(intent);
    }


    public void setByKategori(){


        for (int i = 0; i < kategoriList.size() ; i++) {

            kategori[i] = kategoriList.get(i);
            if(position == i){
                for (int j = 0; j < menuList.size(); j++) {
                    if (menuList.get(j).getIdKategori().toString().equals(kategoriList.get(i).getId().toString())){
                        menuListTemp.add(menuList.get(j));
                    }
                }
            }
        }

    }





}
