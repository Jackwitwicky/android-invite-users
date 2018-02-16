package com.incobeta.inviteusers;

import android.Manifest;
import android.content.ContentResolver;
import android.content.ContentUris;
import android.content.pm.PackageManager;
import android.content.res.TypedArray;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Parcelable;
import android.provider.ContactsContract;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;

import com.dpizarro.autolabel.library.AutoLabelUI;
import com.dpizarro.autolabel.library.Label;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class InviteActivity extends AppCompatActivity {

    //global variables
    private final String KEY_INSTANCE_STATE_PEOPLE = "statePeople";
    private final String TAG = InviteActivity.class.getSimpleName();
    private final int MY_PERMISSIONS_REQUEST_READ_CONTACTS = 1;

    private AutoLabelUI mAutoLabel;
    private List<Person> mPersonList;
    private MyAdapter adapter;
    private RecyclerView recyclerView;
    private List<Person> selectedPersonList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_invite);

        //setup the views
        mAutoLabel = findViewById(R.id.label_view);
        mAutoLabel.setBackgroundResource(R.drawable.round_corner_background);
        recyclerView = findViewById(R.id.recyclerView);


        //add listeners to auto label
        mAutoLabel.setOnLabelsCompletedListener(new AutoLabelUI.OnLabelsCompletedListener() {
            @Override
            public void onLabelsCompleted() {
                Snackbar.make(recyclerView, "Completed!", Snackbar.LENGTH_SHORT).show();
            }
        });

        mAutoLabel.setOnRemoveLabelListener(new AutoLabelUI.OnRemoveLabelListener() {
            @Override
            public void onRemoveLabel(View view, int position) {
                adapter.setItemSelected(position, false);

                //remove person from selected list
                Person deletePerson = mPersonList.get(position);
                selectedPersonList.remove(deletePerson);
            }
        });

        mAutoLabel.setOnLabelsEmptyListener(new AutoLabelUI.OnLabelsEmptyListener() {
            @Override
            public void onLabelsEmpty() {
                Snackbar.make(recyclerView, "EMPTY!", Snackbar.LENGTH_SHORT).show();
            }
        });

        mAutoLabel.setOnLabelClickListener(new AutoLabelUI.OnLabelClickListener() {
            @Override
            public void onClickLabel(View v) {
                Label label = (Label) v;
                Snackbar.make(recyclerView, label.getText(), Snackbar.LENGTH_SHORT).show();
            }
        });

        //setup the recycler view
        LinearLayoutManager llm = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(llm);

        mPersonList = new ArrayList<>();
        selectedPersonList = new ArrayList<>();

        //determine whether to request for permission or proceed
        int permissionCheck = ContextCompat.checkSelfPermission(this,
                Manifest.permission.READ_CONTACTS);

        generateFakeData();

        if (permissionCheck == PackageManager.PERMISSION_GRANTED) {
            getAllContacts();
        }
        else {
            requestContactPermission();
        }

        adapter = new MyAdapter(mPersonList);
        recyclerView.setAdapter(adapter);
        adapter.setOnItemClickListener(new MyAdapter.OnItemClickListener() {

            @Override
            public void onItemClick(View v, int position) {
                itemListClicked(position);
            }
        });
    }

    //generate fake data incase no contacts are available
    private void generateFakeData() {
        //Populate list
        List<String> names = Arrays.asList(getResources().getStringArray(R.array.names));
        List<String> phones = Arrays.asList(getResources().getStringArray(R.array.phones));
        TypedArray photos = getResources().obtainTypedArray(R.array.photos);

        for (int i = 0; i < names.size(); i++) {
            mPersonList.add(new Person(names.get(i), phones.get(i),
                    BitmapFactory.decodeResource(getResources(), photos.getResourceId(i, -1)),
                    false));
        }
    }

    //options menu to invite users

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.activity_invite, menu);

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.action_invite) {
            //user has clicked invite.

            if (selectedPersonList.isEmpty()) {
                Snackbar.make(recyclerView, "No users are selected", Snackbar.LENGTH_SHORT).show();
            }
            else {
                if (selectedPersonList.size() == 1) {
                    Snackbar.make(recyclerView, "Invite 1 User", Snackbar.LENGTH_SHORT).show();
                }
                else {
                    Snackbar.make(recyclerView, "Invite " +
                            selectedPersonList.size() + " Users", Snackbar.LENGTH_SHORT).show();
                }

                for (Person person : selectedPersonList) {
                    Log.i(TAG, person.getName());
                }
            }
            return true;
        }


        return super.onOptionsItemSelected(item);
    }


    //handle when an item has been selected
    private void itemListClicked(int position) {
        Person person = mPersonList.get(position);
        boolean isSelected = person.isSelected();
        boolean success;
        if (isSelected) {
            success = mAutoLabel.removeLabel(position);

            //remove person from selected list
            Person deletePerson = mPersonList.get(position);
            selectedPersonList.remove(deletePerson);
        } else {
            success = mAutoLabel.addLabel(person.getName(), position);

            selectedPersonList.add(mPersonList.get(position)); //add person to selected list
        }
        if (success) {
            adapter.setItemSelected(position, !isSelected);
        }
    }

    private void requestContactPermission() {
        //ask user for permission to access contact list
        if (ContextCompat.checkSelfPermission(this,
                Manifest.permission.READ_CONTACTS)
                != PackageManager.PERMISSION_GRANTED) {

            // Should we show an explanation?
            if (ActivityCompat.shouldShowRequestPermissionRationale(this,
                    Manifest.permission.READ_CONTACTS)) {

                // Show an explanation to the user *asynchronously* -- don't block
                // this thread waiting for the user's response! After the user
                // sees the explanation, try again to request the permission.

            } else {

                // No explanation needed, we can request the permission.

                ActivityCompat.requestPermissions(this,
                        new String[]{Manifest.permission.READ_CONTACTS},
                        MY_PERMISSIONS_REQUEST_READ_CONTACTS);

                // MY_PERMISSIONS_REQUEST_READ_CONTACTS is an
                // app-defined int constant. The callback method gets the
                // result of the request.
            }
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           String permissions[], int[] grantResults) {
        switch (requestCode) {
            case MY_PERMISSIONS_REQUEST_READ_CONTACTS: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                    // permission was granted, get list of contacts
                    getAllContacts();

                } else {
                    // permission denied

                    Snackbar.make(recyclerView,
                            "User cancelled permission to access the contacts",
                            Snackbar.LENGTH_SHORT).show();
                }
                return;
            }

            // other 'case' lines to check for other
            // permissions this app might request.
        }
    }



    private void getAllContacts() {
        Person person;

        ContentResolver contentResolver = getContentResolver();
        Cursor cursor = contentResolver.query(ContactsContract.Contacts.CONTENT_URI, null, null, null,
                ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME + " ASC");

        if( cursor != null && cursor.getCount() > 0) {
            while (cursor.moveToNext()) {
                int hasPhoneNumber = Integer.parseInt(cursor.getString(
                        cursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.HAS_PHONE_NUMBER)));

                //check if user has a phone number
                if(hasPhoneNumber > 0) {
                    String id = cursor.getString(cursor.getColumnIndex(ContactsContract.Contacts._ID));
                    String name = cursor.getString(cursor.getColumnIndex(ContactsContract.Contacts.DISPLAY_NAME));
                    String phoneNumber = "12345678";

                    Cursor phoneCursor = contentResolver.query(
                            ContactsContract.CommonDataKinds.Phone.CONTENT_URI,
                            null,
                            ContactsContract.CommonDataKinds.Phone.CONTACT_ID + " = ?",
                            new String[] {id},
                            null);
                    if (phoneCursor != null && phoneCursor.moveToNext()) {
                        phoneNumber = phoneCursor.getString(
                                phoneCursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER));

                        phoneCursor.close();
                    }

                    person = new Person(name, phoneNumber, getPhoto(id), false);

                    mPersonList.add(person);
                }
            }
        }

        if (cursor != null) {
            cursor.close(); //close the cursor
        }
    }


    //method to get photo of desired user
    private Bitmap getPhoto(String id) {
        Bitmap photo = null;

        try {

            InputStream input =
                    ContactsContract.Contacts.openContactPhotoInputStream (
                            getContentResolver(),
                            ContentUris.withAppendedId(
                                    ContactsContract.Contacts.CONTENT_URI,
                                    Long.parseLong(id)));

            if (input != null) {
                photo = BitmapFactory.decodeStream(input);
                input.close();
            }
            else {
                photo = BitmapFactory.decodeResource(getResources(),
                        R.drawable.ic_contact);
            }



        }

        catch (IOException iox) {
            iox.printStackTrace();
        }

        return photo;
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {

        super.onSaveInstanceState(outState);
        outState.putParcelableArrayList(KEY_INSTANCE_STATE_PEOPLE,
                (ArrayList<? extends Parcelable>) adapter.getPeople());

    }
}
