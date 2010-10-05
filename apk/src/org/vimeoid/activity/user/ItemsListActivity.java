package org.vimeoid.activity.user;

import org.json.JSONException;
import org.json.JSONObject;
import org.vimeoid.R;
import org.vimeoid.activity.base.ItemsListActivity_;
import org.vimeoid.activity.base.ListApiTask_;
import org.vimeoid.activity.base.ListApiTask_.Reactor;
import org.vimeoid.adapter.JsonObjectsAdapter;
import org.vimeoid.util.AdvancedItem;
import org.vimeoid.util.ApiParams;
import org.vimeoid.util.Dialogs;
import org.vimeoid.util.Invoke;

import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.TextView;

public abstract class ItemsListActivity<ItemType extends AdvancedItem> extends 
                      ItemsListActivity_<ItemType, JsonObjectsAdapter<ItemType>,
                                         ApiParams, JSONObject> {
	
    public static final String TAG = "ItemsListActivity";
    
    private String apiMethod;
    private ApiParams params;
    
    protected final ApiTasksQueue secondaryTasks;
    
    public ItemsListActivity(int mainView, int contextMenu) {
    	super(mainView, contextMenu);
    	
    	secondaryTasks = new ApiTasksQueue() {            
            @Override public void onPerfomed(int taskId, JSONObject result) throws JSONException {
                onSecondaryTaskPerfomed(taskId, result);
            }

            @Override public void onError(Exception e, String message) {
                Log.e(TAG, message + " / " + e.getLocalizedMessage());
                Dialogs.makeExceptionToast(ItemsListActivity.this, message, e);
            }
        };
    }
    
    public ItemsListActivity(int contextMenu) {
    	this(R.layout.generic_list, contextMenu);
    }
    
    @Override
    protected ApiParams collectTaskParams(Bundle extras) {
        apiMethod = extras.getString(Invoke.Extras.API_METHOD);
        params = ApiParams.fromBundle(extras.getBundle(Invoke.Extras.API_PARAMS));        

        return params;
    }
    
    /* (non-Javadoc)
     * @see org.vimeoid.activity.base.ItemsListActivity_#executeTask(org.vimeoid.activity.base.ListApiTask_, java.lang.Object)
     */
    @Override
    protected void executeTask(ListApiTask_<ApiParams, JSONObject> task, ApiParams params) {
        task.execute(params);
    }
    
    @Override
    protected ListApiTask_<ApiParams, JSONObject> prepareListTask(
            Reactor<ApiParams, JSONObject> reactor,
            JsonObjectsAdapter<ItemType> adapter) {
        final ListApiTask listTask = new ListApiTask(reactor, adapter, apiMethod);
        listTask.setMaxPages(10);
        listTask.setPerPage(20);
        return listTask;
    }
    
    protected void initTitleBar(ImageView subjectIcon, TextView subjectTitle, ImageView resultIcon) {
        subjectIcon.setImageResource(getIntent().getIntExtra(Invoke.Extras.SUBJ_ICON, R.drawable.info));
        subjectTitle.setText(getIntent().hasExtra(Invoke.Extras.SUBJ_TITLE) 
                             ? getIntent().getStringExtra(Invoke.Extras.SUBJ_TITLE) 
                             : getString(R.string.unknown));
        resultIcon.setImageResource(getIntent().getIntExtra(Invoke.Extras.RES_ICON, R.drawable.info));
    }
    
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater(); //from activity
        inflater.inflate(R.menu.user_options_menu, menu); 
        
        return true;
    }
   
    public void onSecondaryTaskPerfomed(int id, JSONObject result)  throws JSONException { }  
   
    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        return super.onPrepareOptionsMenu(menu);
    }
    
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        
        switch (item.getItemId()) {
            case R.id.menu_Refresh: {
                    Dialogs.makeToast(this, getString(R.string.currently_not_supported)); 
                } break;
            case R.id.menu_Preferences: {
                    Dialogs.makeToast(this, getString(R.string.currently_not_supported)); 
                } break;
            case R.id.menu_SwitchView: {
                    Dialogs.makeToast(this, getString(R.string.currently_not_supported)); 
                } break;
            default: Dialogs.makeToast(this, getString(R.string.unknown_item));
        }         
        return super.onOptionsItemSelected(item);
        
    }
            
}
