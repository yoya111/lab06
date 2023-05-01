package com.example.lab06;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    ArrayAdapter<Note> adp;
    int sel;
    ListView lst;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        adp = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1);
        lst = findViewById(R.id.lst_notes);
        lst.setAdapter(adp);

        lst.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                sel = position;
            }
        });
    }

    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        if (data != null) {
            int pos = data.getIntExtra("my-note-index", -1);
            String title = data.getStringExtra("my-note-title");
            String content = data.getStringExtra("my-note-content");

            Note n = adp.getItem(pos);
            n.title = title;
            n.content = content;

            adp.notifyDataSetChanged();
        }

        super.onActivityResult(requestCode, resultCode, data);
    }

    public void on_new_click(View v) {
        Note n = new Note();
        n.title = "New note";
        n.content = "Some content";

        adp.add(n);
        int pos = adp.getPosition(n);

        Intent i = new Intent(this, Main2Activity.class);
        i.putExtra("my-note-index", pos);
        i.putExtra("my-note-title", n.title);
        i.putExtra("my-note-content", n.content);

        sel = AdapterView.INVALID_POSITION;
        startActivityForResult(i, 12345);
    }

    public void on_edit_click(View v)
    {
        if (sel >= 0)
        {
            Note n = adp.getItem(sel);

            Intent i = new Intent(this, Main2Activity.class);
            i.putExtra("my-note-index", sel);
            i.putExtra("my-note-title", n.title);
            i.putExtra("my-note-content", n.content);

            startActivityForResult(i, 23456);
        }
        else
        {
            Toast.makeText(this, "Выбирай то, что ты хочешь редактировать", Toast.LENGTH_SHORT).show();
            return;
        }
    }


    public void on_delete_click(View v) {
        if (sel == AdapterView.INVALID_POSITION) {
            Toast.makeText(this, "Выбирай то, что ты хочешь удалить", Toast.LENGTH_SHORT).show();
            return;
        }
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Подтверждение удаления");
        builder.setMessage("Вы уверены что хотите это удалить?");
        builder.setPositiveButton("Да", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                adp.remove(adp.getItem(sel));
                adp.notifyDataSetChanged();
                sel = AdapterView.INVALID_POSITION;
            }
        });
        builder.setNegativeButton("Нет", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });

        AlertDialog dialog = builder.create();
        dialog.show();
    }

}
