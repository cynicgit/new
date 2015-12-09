package ip.cynic.mobilesafe.activity;

import ip.cynic.mobilesafe.R;
import ip.cynic.mobilesafe.domain.Contact;

import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.content.ContentResolver;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;

/**
 * @author cynic
 *
 * 2015-12-1
 */
public class ContactActivity extends Activity{

    List<Contact> contactList;
    private ListView lvContact;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contact);
        contactList = new ArrayList<Contact>();
        getContact();
        lvContact = (ListView) findViewById(R.id.lv_contact);
        lvContact.setAdapter(new MyAdapter());
        lvContact.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {
                Contact contact = contactList.get(position);
                Intent intent = new Intent();
                intent.putExtra("phone", contact.getPhone());
                setResult(10, intent);
                finish();
            }
        });
    }

    public void getContact() {
        ContentResolver cr = getContentResolver();
        // 1.先从raw_contacts中获取联系人id
        Cursor rawContactQuery = cr.query(
                Uri.parse("content://com.android.contacts/raw_contacts"),
                new String[] { "contact_id" }, null, null, null);
        String contact_id = "";
        while (rawContactQuery.moveToNext()) {

            contact_id = rawContactQuery.getString(0);
            // 2.通过contact_id查找data表中联系人信息
            Cursor dataQuery = cr.query(
                    Uri.parse("content://com.android.contacts/data"),
                    new String[] { "mimetype", "data1" }, "raw_contact_id=?",
                    new String[] { contact_id }, null);
            Contact contact = new Contact();
            while (dataQuery.moveToNext()) {
                String mimetype = dataQuery.getString(0);
                String data1 = dataQuery.getString(1);
                if ("vnd.android.cursor.item/name".equals(mimetype)) {
                    contact.setName(data1);
                } else if ("vnd.android.cursor.item/phone_v2".equals(mimetype)) {
                    contact.setPhone(data1);
                }
            }
            dataQuery.close();
            contactList.add(contact);
        }
        rawContactQuery.close();
    }

    class MyAdapter extends BaseAdapter {

        @Override
        public int getCount() {
            return contactList.size();
        }

        @Override
        public Object getItem(int position) {
            return null;
        }

        @Override
        public long getItemId(int position) {
            return 0;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {

            View v = null;
            ViewHolder vh = null;
            if (convertView == null) {
                v = View.inflate(ContactActivity.this,
                        R.layout.activity_contact_item, null);
                vh = new ViewHolder();
                vh.tvName = (TextView) v.findViewById(R.id.tv_name);
                vh.tvPhone = (TextView) v.findViewById(R.id.tv_phone);
                v.setTag(vh);
            } else {
                v = convertView;
                vh = (ViewHolder) v.getTag();
            }

            vh.tvName.setText(contactList.get(position).getName());
            vh.tvPhone.setText(contactList.get(position).getPhone());

            return v;
        }

        class ViewHolder {
            TextView tvName;
            TextView tvPhone;
        }

    }
}
