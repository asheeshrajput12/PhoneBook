package com.personalproject.phonebook.app;

import static com.personalproject.phonebook.model.CommonVar.REQUEST_CALL_PHONE_PERMISSION;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.TextView;
import android.widget.Toast;

import com.personalproject.phonebook.databinding.ActivityMainBinding;
import com.personalproject.phonebook.databinding.ItemContactBinding;
import com.personalproject.phonebook.model.CommonVar;
import com.personalproject.phonebook.model.Contact;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {
    private ActivityMainBinding binding;
    private ArrayList<Contact> contactList;
    private NumberAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        // Initialize views and adapters
        contactList = new ArrayList<>();
        adapter = new NumberAdapter(contactList);
        binding.numberRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        binding.numberRecyclerView.setAdapter(adapter);

        binding.addButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addContact();
            }
        });
        binding.fabDialer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               /* openDialer();*/
                toggleDialer();
            }
        });
    }
    private boolean isDialerVisible = false;
    private void toggleDialer() {
        isDialerVisible = !isDialerVisible;
        binding.rlDailerView.setVisibility(isDialerVisible ? View.VISIBLE : View.GONE);

    }

    private void openDialer() {
        Intent dialerIntent = new Intent(Intent.ACTION_DIAL);
        startActivity(dialerIntent);
    }

    private void addContact() {
        String name = binding.nameEditText.getText().toString().trim();
        String phoneNumber = binding.phoneEditText.getText().toString().trim();

        if (!name.isEmpty() && !phoneNumber.isEmpty()) {
            Contact contact = new Contact(name, phoneNumber);
            contactList.add(contact);
            adapter.notifyDataSetChanged();
            clearEditTextFields();
        }
    }

    private void clearEditTextFields() {
        binding.nameEditText.getText().clear();
        binding.phoneEditText.getText().clear();
    }
    public class NumberAdapter extends RecyclerView.Adapter<NumberAdapter.NumberViewHolder> {

        private ArrayList<Contact> contactArrayList;
        public NumberAdapter(ArrayList<Contact> numbers) {
            this.contactArrayList = numbers;
        }

        @NonNull
        @Override
        public NumberViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            //View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_contact, parent, false);
            ItemContactBinding itemContactBinding=ItemContactBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false);
            return new NumberViewHolder(itemContactBinding);
        }

        @Override
        public void onBindViewHolder(@NonNull NumberViewHolder holder, int position) {
            Contact number = contactArrayList.get(position);
            holder.bind(number);
        }

        @Override
        public int getItemCount() {
            return contactArrayList!=null ? contactArrayList.size():0;
        }

        class NumberViewHolder extends RecyclerView.ViewHolder {
            private final ItemContactBinding binding;
            public NumberViewHolder(@NonNull ItemContactBinding binding) {
                super(binding.getRoot());
                this.binding = binding;
                itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        // Handle item click and initiate a phone call
                        int position = getAdapterPosition();
                        String phoneNumberString = String.valueOf(contactArrayList.get(position).getPhoneNumber());
                        Uri dialUri = Uri.parse("tel:" + phoneNumberString);
                        // Create the call intent
                        Intent callIntent = new Intent(Intent.ACTION_CALL, dialUri);

                        // Check if the CALL_PHONE permission is granted
                        if (ContextCompat.checkSelfPermission(getApplicationContext(), android.Manifest.permission.CALL_PHONE) == 0) {
                            // Start the phone call
                            MainActivity.this.startActivity(callIntent);
                        } else {
                            // Handle permission not granted
                            // You may want to request the permission at runtime here
                            // or inform the user to grant the permission in the app settings.
                            // Request the CALL_PHONE permission at runtime
                            ActivityCompat.requestPermissions(MainActivity.this, new String[]{android.Manifest.permission.CALL_PHONE}, CommonVar.REQUEST_CALL_PHONE_PERMISSION);
                        }

                    }
                });
            }


            public void bind(Contact number) {
                binding.nameTextView.setText(number.getName());
                binding.phoneTextView.setText(number.getPhoneNumber());

                // Set click listener for the settings icon
                binding.ivItemSetting.setOnClickListener(v -> {
                    // Handle settings icon click
                    Intent intent = new Intent(MainActivity.this, ActivityContactView.class);
                    intent.putExtra(CommonVar.STR_NAME_PERSON,number.getName());
                    intent.putExtra(CommonVar.STR_NUMBER_PERSON,number.getPhoneNumber());
                    startActivity(intent);
                });
            }
        }
    }
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == REQUEST_CALL_PHONE_PERMISSION) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                // Permission granted, initiate the phone call
                /*int position = adapter.getCurrentPosition(); // Assume you have a method to get the current position
                int phoneNumber = numbers.get(position);
                adapter.initiatePhoneCall(phoneNumber);*/
            } else {
                // Permission denied, show a message or take appropriate action
                Toast.makeText(this, "Call permission denied", Toast.LENGTH_SHORT).show();
            }
        }
    }

}