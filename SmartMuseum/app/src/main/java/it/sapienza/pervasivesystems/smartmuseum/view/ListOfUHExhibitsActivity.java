package it.sapienza.pervasivesystems.smartmuseum.view;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ListView;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;

import it.sapienza.pervasivesystems.smartmuseum.R;
import it.sapienza.pervasivesystems.smartmuseum.SmartMuseumApp;
import it.sapienza.pervasivesystems.smartmuseum.business.InternalStorage.FileSystemBusiness;
import it.sapienza.pervasivesystems.smartmuseum.business.exhibits.ExhibitBusiness;
import it.sapienza.pervasivesystems.smartmuseum.business.exhibits.WorkofartBusiness;
import it.sapienza.pervasivesystems.smartmuseum.business.interlayercommunication.ILCMessage;
import it.sapienza.pervasivesystems.smartmuseum.model.adapter.VisitExhibitModelArrayAdapter;
import it.sapienza.pervasivesystems.smartmuseum.model.entity.VisitExhibitModel;
import it.sapienza.pervasivesystems.smartmuseum.model.entity.VisitWorkofartModel;
import it.sapienza.pervasivesystems.smartmuseum.view.slack.gui.MainChatActivity;

public class ListOfUHExhibitsActivity extends AppCompatActivity implements ListOfUHExhibitsActivityLoadUserHistoryAsyncResponse {

    private ListView listView;
    VisitExhibitModelArrayAdapter exhibitVisitsAdapter;
    List<VisitExhibitModel> dataItems = null;
    private ProgressDialog progressDialog;

    private Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_of_uh_exhibits);

        this.progressDialog = new ProgressDialog(ListOfUHExhibitsActivity.this, R.style.AppTheme_Dark_Dialog);
        this.progressDialog.setIndeterminate(true);
        this.progressDialog.setMessage("Loading User History... Please wait.");
        this.progressDialog.show();

        new ListOfUHExhibitsActivityLoadUserHistoryAsync(this).execute();

        toolbar = (Toolbar) findViewById(R.id.tool_bar); // Attaching the layout to the toolbar object
        setSupportActionBar(toolbar);

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);

        toolbar.getMenu().findItem(R.id.exhibition_list).setVisible(false);
        this.createThreadChatNotification();
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.

        int id = item.getItemId();

        if (id == R.id.action_logout) {
            try {
                new FileSystemBusiness(this).deleteFile(SmartMuseumApp.localLoginFile);
            } catch (IOException e) {
                e.printStackTrace();
            }
            Intent intent = new Intent(this, LoginActivity.class);
            this.startActivity(intent);
        }
        if(id == R.id.action_ask) {
            SmartMuseumApp.newMessageRead = true;
            Intent intent = new Intent(this, MainChatActivity.class);
            this.startActivity(intent);
        }

        if (id == R.id.work_of_art_list) {
            Intent intent = new Intent(this, ListOfUHObjectsActivity.class);
            this.startActivity(intent);
        }

        if(id == R.id.action_back) {
            super.onBackPressed();
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void loadUserHistoryFinish(ILCMessage message) {
        Object[] objects = (Object[]) message.getMessageObject();
        List<VisitExhibitModel> ev = null;

        this.dataItems = (List<VisitExhibitModel>) objects[0];
        exhibitVisitsAdapter = new VisitExhibitModelArrayAdapter(ListOfUHExhibitsActivity.this, R.layout.activity_item_of_exhibits, dataItems);

        SmartMuseumApp.totalVisitedWorksofart = (HashMap<String, VisitWorkofartModel>) objects[1];

        // Getting a reference to listview of activity_item_of_exhibits layout file
        listView = (ListView) findViewById(R.id.listview);
        listView.setItemsCanFocus(false);
        listView.setAdapter(exhibitVisitsAdapter);

        this.progressDialog.dismiss();
    }

    private void createThreadChatNotification() {
        final ListOfUHExhibitsActivity parentActivity = this;
        final Handler handler=new Handler();
        handler.post(new Runnable(){
            @Override
            public void run() {
                // upadte textView here
                handler.postDelayed(this,5000); // set time here to refresh textView

                MenuItem item = toolbar.getMenu().findItem(R.id.action_ask);

                if(SmartMuseumApp.newMessage) {
                    SmartMuseumApp.newMessageRead = false;
                    item.setIcon(R.drawable.ic_chat_notification2);
                }
                else {
                    if(!SmartMuseumApp.newMessageRead)
                        item.setIcon(R.drawable.ic_chat_notification2);
                    else
                        item.setIcon(R.drawable.chat_icon);
                }
            }
        });
    }
}

/* Load User Exhibit History: exhibits he already visited today */
interface ListOfUHExhibitsActivityLoadUserHistoryAsyncResponse {
    void loadUserHistoryFinish(ILCMessage message);
}

class ListOfUHExhibitsActivityLoadUserHistoryAsync extends AsyncTask<Void, Integer, String> {

    private ListOfUHExhibitsActivityLoadUserHistoryAsyncResponse delegate;
    private ILCMessage message = new ILCMessage();

    public ListOfUHExhibitsActivityLoadUserHistoryAsync(ListOfUHExhibitsActivityLoadUserHistoryAsyncResponse d) {
        this.delegate = d;
    }

    @Override
    protected String doInBackground(Void... voids) {
        this.message.setMessageType(ILCMessage.MessageType.INFO);
        this.message.setMessageText("List of total exhibit user history");
        ExhibitBusiness exhibitBusiness = new ExhibitBusiness();
        List<VisitExhibitModel> totalVisits = exhibitBusiness.getUserExhibitHistoryList(SmartMuseumApp.loggedUser);

        WorkofartBusiness workofartBusiness = new WorkofartBusiness();
        HashMap<String, VisitWorkofartModel> totalWorkofartsVisits = workofartBusiness.getTotalUserWorksofartHistoryHashMap(SmartMuseumApp.loggedUser);

        Object[] objects = new Object[] {totalVisits, totalWorkofartsVisits};

        this.message.setMessageObject(objects);
        return this.message.getMessageText();
    }

    @Override
    protected void onPostExecute(String result) {
        this.delegate.loadUserHistoryFinish(this.message);
    }
}


