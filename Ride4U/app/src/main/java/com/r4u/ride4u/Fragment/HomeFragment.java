package com.r4u.ride4u.Fragment;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import androidx.appcompat.widget.SearchView;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.r4u.ride4u.Post;
import com.r4u.ride4u.R;
import java.util.ArrayList;


public class HomeFragment extends Fragment {

    SearchView searchView;
    ListView listView;
    DatabaseReference databaseReference = FirebaseDatabase.getInstance("https://ride4u-3a773-default-rtdb.europe-west1.firebasedatabase.app/").getReference();
    ArrayList<Post> posts;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        // Inflate the layout for this fragment
<<<<<<< HEAD
        return inflater.inflate(R.layout.fragment_home, container, false);
=======
        View view = inflater.inflate(R.layout.fragment_home, container, false);
>>>>>>> e0f1903f44f310cdd01deee19f29685a2a4ac934



    }

    public void onViewCreated(View view,Bundle bundle){
        initPostList();

        searchView = view.findViewById(R.id.search_bar);
        listView = view.findViewById(R.id.list_view);
<<<<<<< HEAD
        ListViewBaseAdapter baseAdapter = new ListViewBaseAdapter(getActivity().getApplicationContext(), posts);
        listView.setAdapter(baseAdapter);
        baseAdapter.notifyDataSetChanged();
        listView.requestLayout();

=======
        listView.requestLayout();
        ListViewBaseAdapter baseAdapter = new ListViewBaseAdapter(getActivity().getApplicationContext(), posts);
        listView.setAdapter(baseAdapter);
        baseAdapter.notifyDataSetChanged();

        return view;
>>>>>>> e0f1903f44f310cdd01deee19f29685a2a4ac934
    }

    private void initPostList() {
        posts = new ArrayList<>();
        databaseReference.child("posts").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapShot) {
                for(DataSnapshot snapshot : dataSnapShot.getChildren()) {
                    posts.add(new Post(snapshot.getKey(), snapshot.child("firstname").getValue(String.class), snapshot.child("lastname").getValue(String.class), "desc", snapshot.child("src").getValue(String.class), snapshot.child("dest").getValue(String.class)));
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                System.out.println(error.getMessage());
            }
        });

    }
}