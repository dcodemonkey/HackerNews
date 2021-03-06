package nirmesh.rahul.hackernews.job;

import com.path.android.jobqueue.Job;
import com.path.android.jobqueue.Params;

import de.greenrobot.event.EventBus;
import retrofit.RestAdapter;
import nirmesh.rahul.hackernews.Utls;
import nirmesh.rahul.hackernews.event.ErrorEvent;

/**
 * Created by NIRMESH on 1/1/2017.
 */
public abstract class ApiJob extends Job {
    protected RestAdapter adapter;
    protected boolean ShowError = true;

    protected ApiJob() {
        super(new Params(1).requireNetwork());
        adapter = Utls.GetRestAdapter();
    }

    @Override
    protected boolean shouldReRunOnThrowable(Throwable throwable) {
        String cause = throwable.getMessage();
        
		//for troubleshooting
        if (Utls.NotNullEmpty(cause) && cause.toLowerCase().contains("401")) {
            return false;
        } else if (Utls.NotNullEmpty(cause) && cause.toLowerCase().contains("404")) {
            return false;
        } else if (Utls.NotNullEmpty(cause) && cause.toLowerCase().contains("500")) {
            return false;
        } else if (Utls.NotNullEmpty(cause) && cause.toLowerCase().contains("408")) {
            return true;//request timeout
        }
        //show error message for all other error
        EventBus.getDefault().post(new ErrorEvent(throwable.getLocalizedMessage()));
        return false;
    }
}
