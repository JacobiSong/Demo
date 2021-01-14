package com.example.demo.ui.mine.profile;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.content.FileProvider;
import androidx.fragment.app.Fragment;

import android.provider.MediaStore;
import android.util.Base64;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.example.demo.MyApplication;
import com.example.demo.R;
import com.google.protobuf.ByteString;
import com.yalantis.ucrop.UCrop;
import com.yalantis.ucrop.UCropActivity;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.nio.ByteBuffer;

import de.hdodenhof.circleimageview.CircleImageView;

public class UserProfileFragment extends Fragment {

    private static final int CAMERA_OK = 100;
    private static final int ALBUM_OK = 101;
    private static final int TAKE_PHOTO = 200;
    private static final int CHOOSE_PHOTO = 201;
    private static final String TEMPORARY_NAME = "temporary.jpg";
    private static final String CROP_NAME = "crop.jpg";

    public static UserProfileFragment newInstance() {
        return new UserProfileFragment();
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.user_profile_fragment, container, false);
        String userId = MyApplication.getUsername();
        UserProfileItem id = root.findViewById(R.id.user_id);
        UserProfileItem name = root.findViewById(R.id.user_name);
        UserProfileItem gender = root.findViewById(R.id.user_gender);
        UserProfileItem phone = root.findViewById(R.id.user_phone);
        UserProfileItem email = root.findViewById(R.id.user_email);
        UserProfileItem department = root.findViewById(R.id.user_department);
        UserProfileItem major = root.findViewById(R.id.user_major);
        UserProfileItem classNo = root.findViewById(R.id.user_class_no);
        UserProfileItem password = root.findViewById(R.id.user_password);
        RelativeLayout relativeLayout = root.findViewById(R.id.user_photo_layout);
        CircleImageView userPhoto = root.findViewById(R.id.user_photo);
        relativeLayout.setOnTouchListener((v, event) -> {
            switch (event.getAction()) {
                case MotionEvent.ACTION_DOWN:
                    relativeLayout.setBackgroundColor(getResources().getColor(R.color.gray));
                    break;
                case MotionEvent.ACTION_MOVE:
                    break;
                case MotionEvent.ACTION_UP:
                    relativeLayout.setBackgroundColor(getResources().getColor(R.color.white));
                    break;
                default:
                    break;
            }
            return false;
        });
        relativeLayout.setOnClickListener(v -> {
            View view = View.inflate(requireActivity(), R.layout.photo_method_choice, null);
            final AlertDialog alertDialog = new AlertDialog.Builder(requireActivity()).setView(view).create();
            view.findViewById(R.id.choose_from_album).setOnClickListener(view1 -> {
                choosePhoto();
                alertDialog.dismiss();
            });
            view.findViewById(R.id.take_photo).setOnClickListener(v1 -> {
                if (ContextCompat.checkSelfPermission(requireActivity(), Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
                    ActivityCompat.requestPermissions(requireActivity(), new String[]{Manifest.permission.CAMERA}, CAMERA_OK);
                } else {
                    takePhoto();
                }
                alertDialog.dismiss();
            });
            view.findViewById(R.id.cancel).setOnClickListener(v12 -> alertDialog.dismiss());
            alertDialog.show();
        });
        id.setText(userId);
        Cursor c = MyApplication.getDatabase().query("select name from user where id = ?", userId);
        if (c.moveToFirst()) {
            name.setText(c.getString(0));
        }

        gender.setOnClickListener(v -> startActivity(
                new Intent(requireActivity(), UserProfileChangeActivity.class).setAction("com.example.demo.user.change.gender").putExtra("text", gender.getText())));
        phone.setOnClickListener(v -> startActivity(
                new Intent(requireActivity(), UserProfileChangeActivity.class).setAction("com.example.demo.user.change.phone").putExtra("text", phone.getText())));
        email.setOnClickListener(v -> startActivity(
                new Intent(requireActivity(), UserProfileChangeActivity.class).setAction("com.example.demo.user.change.email").putExtra("text", email.getText())));
        department.setOnClickListener(v -> startActivity(
                new Intent(requireActivity(), UserProfileChangeActivity.class).setAction("com.example.demo.user.change.department").putExtra("text", department.getText())));
        password.setOnClickListener(v -> startActivity(
                new Intent(requireActivity(), UserProfileChangeActivity.class).setAction("com.example.demo.user.change.password")
        ));

        MyApplication.getDatabase().createQuery("user", "select gender, phone, email from user where id = ?", userId).subscribe(query -> {
            Cursor cursor = query.run();
            if (cursor.moveToFirst()) {
                int g = cursor.getInt(0);
                if (getActivity() != null) {
                    getActivity().runOnUiThread(() -> {
                        gender.setText(g == 0 ? "保密" : g == 1 ? "女" : "男");
                        phone.setText(cursor.getString(1));
                        email.setText(cursor.getString(2));
                        SharedPreferences sp = getActivity().getSharedPreferences("user_" + MyApplication.getUsername(), Context.MODE_PRIVATE);
                        String str = sp.getString("photo", "");
                        if (str.isEmpty()) {
                            userPhoto.setImageDrawable(getResources().getDrawable(R.drawable.account_circle_80));
                        } else {
                            byte[] bytes = Base64.decode(str.getBytes(), Base64.DEFAULT);
                            userPhoto.setImageBitmap(BitmapFactory.decodeByteArray(bytes, 0, bytes.length));
                        }
                    });
                }
            }
        });

        if (requireActivity().getSharedPreferences("user_" + MyApplication.getUsername(), Context.MODE_PRIVATE).getInt("identity", 0) == 1) {
            major.setVisibility(View.GONE);
            classNo.setVisibility(View.GONE);
            MyApplication.getDatabase().createQuery("user", "select department from teacher where id = ?", userId).subscribe(query -> {
                Cursor cursor = query.run();
                if (cursor.moveToFirst()) {
                    if (getActivity() != null) {
                        getActivity().runOnUiThread(() -> department.setText(cursor.getString(0)));
                    }
                }
            });

        } else {
            major.setOnClickListener(v -> startActivity(
                    new Intent(requireActivity(), UserProfileChangeActivity.class).setAction("com.example.demo.user.change.major").putExtra("text", major.getText())));
            classNo.setOnClickListener(v -> startActivity(
                    new Intent(requireActivity(), UserProfileChangeActivity.class).setAction("com.example.demo.user.change.class_no").putExtra("text", classNo.getText())));
            MyApplication.getDatabase().createQuery("user", "select department, major, class_no from student where id = ?", userId).subscribe(query -> {
                Cursor cursor = query.run();
                if (cursor.moveToFirst()) {
                    if (getActivity() != null) {
                        getActivity().runOnUiThread(() -> {
                            department.setText(cursor.getString(0));
                            major.setText(cursor.getString(1));
                            classNo.setText(cursor.getString(2));
                        });
                    }
                }
            });
        }
        return root;
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode) {
            case CAMERA_OK:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED){
                    takePhoto();
                } else {
                    Toast.makeText(requireActivity(),"申请权限被拒绝",Toast.LENGTH_SHORT).show();
                }
                break;
            case ALBUM_OK:
                if (grantResults.length > 0 )
                break;
            default:
                break;
        }
    }

    private void takePhoto() {
        Intent intent = new Intent();
        intent.setAction(MediaStore.ACTION_IMAGE_CAPTURE);
        intent.putExtra(MediaStore.EXTRA_OUTPUT, FileProvider.getUriForFile(requireContext(), "com.example.demo.fileProvider", new File(requireActivity().getExternalCacheDir() + File.separator, TEMPORARY_NAME)));
        startActivityForResult(intent, TAKE_PHOTO);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        if (resultCode == Activity.RESULT_OK) {
            switch (requestCode) {
                case TAKE_PHOTO: {
                    UCrop.Options options = new UCrop.Options();
                    options.setAllowedGestures(UCropActivity.ALL, UCropActivity.NONE, UCropActivity.ALL);
                    options.setToolbarTitle("头像裁剪");
                    options.setHideBottomControls(true);
                    options.setShowCropGrid(false);
                    options.setCircleDimmedLayer(true);
                    options.setShowCropFrame(false);
                    UCrop.of(Uri.fromFile(new File(requireContext().getExternalCacheDir() + File.separator, TEMPORARY_NAME)),
                            Uri.fromFile(new File(requireContext().getExternalCacheDir() + File.separator, CROP_NAME)))
                            .withAspectRatio(1, 1)
                            .withOptions(options)
                            .start(requireContext(), this);
                    break;
                }
                case CHOOSE_PHOTO: {
                    UCrop.Options options = new UCrop.Options();
                    options.setAllowedGestures(UCropActivity.ALL, UCropActivity.NONE, UCropActivity.ALL);
                    options.setToolbarTitle("头像裁剪");
                    options.setHideBottomControls(true);
                    options.setShowCropGrid(false);
                    options.setCircleDimmedLayer(true);
                    options.setShowCropFrame(false);
                    assert data != null;
                    UCrop.of(data.getData(), Uri.fromFile(new File(requireContext().getExternalCacheDir() + File.separator, CROP_NAME)))
                            .withAspectRatio(1, 1)
                            .withOptions(options)
                            .start(requireContext(), this);
                    break;
                }
                case UCrop.REQUEST_CROP: {
                    try {

                        Bitmap bitmap = BitmapFactory.decodeStream(requireContext().getContentResolver().openInputStream(UCrop.getOutput(data)));
                        ByteArrayOutputStream baos = new ByteArrayOutputStream();
                        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos);
                        byte[] bytes = baos.toByteArray();
                        MyApplication.getServer().changeUserPhoto(bytes);
                    } catch (FileNotFoundException ignored) {

                    }
                    break;
                }
                default:
                    break;
            }
        }
    }

    public void choosePhoto() {
        startActivityForResult(new Intent(Intent.ACTION_PICK).setType("image/*"), CHOOSE_PHOTO);
    }
}