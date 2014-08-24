package klusman.scaantirevents.mobile.Tasks;


import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.Fragment;
import android.app.FragmentManager;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.DialogInterface.OnCancelListener;
import android.content.res.Resources;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.util.Log;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.protocol.HTTP;
import org.xml.sax.SAXException;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import klusman.scaantirevents.R;


abstract public class TaskFragment_new<T> extends Fragment implements OnCancelListener
{

    static final String SERVER_URL = "http://scalac.herokuapp.com";
    protected List<NameValuePair> params = new ArrayList<NameValuePair>();
    boolean mReady = false;
    boolean mQuiting = false;
    TimeoutHandler timeoutHandler;
    private final BaseTask task = new BaseTask();
    private ProgressDialog progress;
    private Dialog errorDialog;
    private Exception exception;
    //private OnTaskCompleteListener mListener;
    private T result;
    private boolean userCancelled;

    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        timeoutHandler = new TimeoutHandler();
        super.onCreate(savedInstanceState);
        // Tell the framework to try to keep this fragment around
        // during a configuration change.
        setRetainInstance(true);


        // Start up the worker thread.
        task.execute();

    }

    /**
     * This is called when the Fragment's Activity is ready to go, after its
     * content view has been installed; it is called both after the initial
     * fragment creation and after the fragment is re-attached to a new
     * activity.
     */
    @Override
    public void onActivityCreated(Bundle savedInstanceState)
    {
        super.onActivityCreated(savedInstanceState);

        if (task.isRunning())
        {
            if (shouldShowProgressDialog())
            {
                progress = new ProgressDialog(getActivity());
                String title = getProgressTitle();

                if (!TextUtils.isEmpty(title))
                    progress.setTitle(title);

                progress.setCancelable(isCancellable());
                progress.setOnCancelListener(this);
                progress.setMessage(getProgressMessage());
                progress.show();
            }
            else
            {
                ((OnTaskCompleteListener) getActivity()).setInProgress(true);
            }
        }

        if (exception != null)
        {
            onTaskError();
        }

        //We are ready for our thread to go.
        synchronized (task)
        {
            mReady = true;
            //mThread.notify();
        }
    }

    protected String getProgressMessage()
    {
        Resources r = getResources();
        if (r != null)
            return r.getString(R.string.progress_msg);
        return "Please wait...";
    }

    protected String getProgressTitle()
    {
        Resources r = getResources();
        if (r != null)
            return r.getString(R.string.progress);
        return "Progress";
    }

    protected boolean isCancellable()
    {
        return true;
    }

    /**
     * This is called when the fragment is going away. It is NOT called when the
     * fragment is being propagated between activity instances.
     */
    @Override
    public void onDestroy()
    {
        // Make the thread go away.
        synchronized (task)
        {
            mReady = false;
            mQuiting = true;
            //mThread.notify();
        }
        //   L.d("<< DESTORY " + getClass().getSimpleName());
        super.onDestroy();
    }

    /**
     * This is called right before the fragment is detached from its current
     * activity instance.
     */
    @Override
    public void onDetach()
    {
        // This fragment is being detached from its activity. We need
        // to make sure its thread is not going to touch any activity
        // state after returning from this function.

        synchronized (task)
        {
            //mProgressBar = null;
            mReady = false;
            if (progress != null)
            {
                progress.dismiss();
                progress = null;
            }
            if (errorDialog != null)
            {
                errorDialog.dismiss();
                errorDialog = null;
            }
        }
        super.onDetach();
    }

    protected void onPreTaskStart()
    {
    }

    protected void onTaskStartInBackground()
    {
    }

    protected void onTaskSuccess()
    {
    }

    protected String getPositiveButtonErrorDialogTitle()
    {
        return "Close";
    }

    protected String getNegativeButtonErrorDialogTitle()
    {
        return null;
    }

    protected DialogInterface.OnClickListener getErrorDialogPositiveOnClickListener()
    {
        return null;
    }

    protected DialogInterface.OnClickListener getErrorDialogNegativeOnClickListener()
    {
        return null;
    }

    protected String getErrorDialogTitle()
    {
        Resources r = getResources();
        if (r != null)
            return r.getString(R.string.error);
        return "Error";
    }

    protected void onTaskError()
    {
        Activity a = getActivity();

        if (a != null)
        {
            if (shouldShowErrorDialog())
            {
                AlertDialog.Builder builder = new AlertDialog.Builder(a)
                        .setTitle(getErrorDialogTitle())
                        .setMessage(getErrorMessage());
                if (!TextUtils.isEmpty(getPositiveButtonErrorDialogTitle()))
                    builder.setPositiveButton(getPositiveButtonErrorDialogTitle(),
                            getErrorDialogPositiveOnClickListener());

                if (!TextUtils.isEmpty(getNegativeButtonErrorDialogTitle()))
                    builder.setNegativeButton(getNegativeButtonErrorDialogTitle(),
                            getErrorDialogNegativeOnClickListener());

                errorDialog = builder.create();
                errorDialog.setOnDismissListener(getErrorDialogOnDismissListener());
                errorDialog.show();
            }
            else
            {
                detach(false);
            }
        }
    }

    protected boolean shouldShowErrorDialog()
    { return true; }

    protected void onTaskEnd()
    {
    }

    protected DialogInterface.OnDismissListener getErrorDialogOnDismissListener()
    {
        return new DialogInterface.OnDismissListener()
        {
            @Override
            public void onDismiss(DialogInterface dialog)
            {
                detach(false);
            }
        };
    }

    //override this to change the message
    public String getErrorMessage()
    {
        return exception.getMessage();
    }

    public T getResult()
    {
        return result;
    }

    public boolean shouldShowProgressDialog()
    { return true; }

    private void detach(boolean success)
    {
        onDetach();
        OnTaskCompleteListener a = (OnTaskCompleteListener) getActivity();
        if (a != null && a.isActive())
        {
            if (success)
                a.onTaskComplete(TaskFragment_new.this);
            else
                a.onTaskError(TaskFragment_new.this);

            FragmentManager mgr = getFragmentManager();

            if (mgr != null)
                mgr.beginTransaction().remove(TaskFragment_new.this).commit();
        }
    }

    public T doBackgroundWork() throws Exception
    {

        T loaded = loadFromLocal();
        if (loaded != null) return loaded;


        timeoutHandler.sendEmptyMessageDelayed(0, getConnectionTimeout() * 1000);
        InputStream is;
        try {
            is = doConnect();
        } catch (IOException ex) {
            Log.i("TAG", getClass().getName(), ex);
            Activity a = getActivity();
            String msg;
            if (a != null)
                msg = a.getString(R.string.connection_problem);
            else
                msg = "Connection problem";
            throw new TaskException(msg);
        }
        timeoutHandler.removeMessages(0);
        return parseResponse(is);
    }

    @Override
    public void onCancel(DialogInterface arg0) {
        cancelTask();
        userCancelled = true;
    }

    private boolean cancelTask() {
        if (task.isRunning()) {
            task.cancel(true);
            return true;
        }
        return false;
    }

    protected boolean forceCancel(String reason) {
        boolean res = cancelTask();
        if (res)
            exception = new RuntimeException(reason);
        return res;
    }

    @Override
    public void onResume() {
        super.onResume();
        if (!task.isRunning())
            task.endTask();
    }

    protected InputStream doConnect() throws IOException {
        return connect(getUri());
    }

    protected void addParameter(String key, String val) {
        params.add(new BasicNameValuePair(key, val));
    }

    private InputStream connect(String uri) throws IOException {
        HttpPost post = new HttpPost(uri);
        post.setEntity(new UrlEncodedFormEntity(params, HTTP.UTF_8));
        return processRequest(post);
    }

    protected InputStream processRequest(HttpUriRequest request) throws IOException {
        HttpClient httpclient = new DefaultHttpClient();
        HttpResponse response = httpclient.execute(request);

        if (response.getStatusLine().getStatusCode() != 200) {
            throw new TaskException(response.getStatusLine().getReasonPhrase());
        }


        // Get hold of the response entity
        HttpEntity entity = response.getEntity();
        // If the response does not enclose an entity, there is no need
        // to worry about connection release
        return entity != null ? entity.getContent() : null;
    }

    protected int getConnectionTimeout() {
        return 45;
    }

    abstract public String getUri();

    protected String extractJSON(InputStream is) throws SAXException, IOException {
        return new JsonHelper.JsonExtractor().parse(is);
    }

    protected abstract T handleJSON(String response) throws IOException;

    private T parseResponse(InputStream is) {
        //String response = convertStreamToString(is);
        try {
            return handleJSON(extractJSON(is));
        } catch (com.google.gson.JsonParseException ex) {
            // L.d(getClass().getName(), ex);
            throw new TaskException(/*stripErrMsg(response)*/"Response error (1)");
        } catch (SAXException e) {
            //  L.d(getClass().getName(), e);
            throw new TaskException(/*stripErrMsg(response)*/"Response error (2)");
        } catch (IOException e) {
            // L.d(getClass().getName(), e);
            throw new TaskException(/*stripErrMsg(response)*/"Response error (3)");
        }
    }

    abstract public T loadFromLocal();


    public interface OnTaskCompleteListener {
        public void onTaskComplete(TaskFragment_new<?> fragment);

        public void onTaskError(TaskFragment_new<?> fragment);

        public boolean isActive();

        public void setInProgress(boolean inProgress);
    }

    public static class TaskException extends RuntimeException {
       // private static final long serialVersionUID = -9210722633737500459L;

        public TaskException(String msg) {
            super(msg);
        }
    }

    protected class BaseTask extends AsyncTask<Void, Void, T> {

        private boolean running;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            onPreTaskStart();
            running = true;
        }

        @Override
        protected void onPostExecute(T res) {
            onTaskEnded(res);
        }

        private void onTaskEnded(T res) {
            running = false;
            result = res;
            endTask();
        }

        private void endTask() {
            if (progress != null)
                progress.dismiss();
            Activity a = getActivity();
            if (a != null)
                ((OnTaskCompleteListener) getActivity()).setInProgress(false);
                onTaskEnd();
            if (userCancelled) {
                detach(false);
                return;
            }
            if (exception == null) {
                onTaskSuccess();
                detach(true);
            } else {
                onTaskError();
            }
        }

        @Override
        protected T doInBackground(Void... params) {
            onTaskStartInBackground();
            try {
                return doBackgroundWork();
            } catch (Exception ex) {
                exception = ex;
            }
            return null;
        }

        public boolean isRunning() {
            return running;
        }

        @Override
        protected void onCancelled() {
            super.onCancelled();
            onTaskEnded(null);
        }
    }

    class TimeoutHandler extends Handler {
        @Override
        public void handleMessage(Message msg) {
            boolean res = forceCancel("Connection timeout...");
        }
    }
}
