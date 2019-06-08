package android.jwn.myfirebasedbrealtime;

import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class MainActivity extends AppCompatActivity {

        EditText edt_title,edt_content;
        Button btn_post;
        RecyclerView recyclerView;

    //Firebase
    FirebaseDatabase firebaseDatabase;
    DatabaseReference databaseReference;

    FirebaseRecyclerOptions<Post> options;
    FirebaseRecyclerAdapter<Post,MyRecyclerViewHolder> adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        edt_content = (EditText)findViewById(R.id.edt_content);
        edt_title = (EditText)findViewById(R.id.edt_title);
        btn_post = (Button)findViewById(R.id.btn_post);
        recyclerView = (RecyclerView)findViewById(R.id.recycler_vew);

        firebaseDatabase = FirebaseDatabase.getInstance();
        databaseReference = firebaseDatabase.getReference("FB_RT");

        btn_post.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                postComment();

            }
        });

        displayComment();

    }

    private void postComment() {
        String title = edt_title.getText().toString();
        String content = edt_content.getText().toString();

        Post post = new Post(title,content);

        //Create a unique id for each comment
        databaseReference.push().setValue(post);

        adapter.notifyDataSetChanged();

    }

    private void displayComment() {
         options =
              new FirebaseRecyclerOptions.Builder<Post>()
                .setQuery(databaseReference,Post.class)
                .build();

         adapter =
                new FirebaseRecyclerAdapter<Post, MyRecyclerViewHolder>(options) {
                    @Override
                    protected void onBindViewHolder(@NonNull MyRecyclerViewHolder holder, int position, @NonNull Post model) {
                        holder.txt_title.setText(model.getTitle());
                        holder.txt_comment.setText(model.getContent());
                    }

                    @NonNull
                    @Override
                    public MyRecyclerViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
                        View itemView = LayoutInflater.from(getBaseContext()).inflate(R.layout.post_item,viewGroup,false);
                        return new MyRecyclerViewHolder(itemView);
                    }
                };

        adapter.startListening();
        recyclerView.setAdapter(adapter);

    }


}

