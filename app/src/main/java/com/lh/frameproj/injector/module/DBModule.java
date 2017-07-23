package com.lh.frameproj.injector.module;

import android.content.Context;

import com.lh.frameproj.db.DaoMaster;
import com.lh.frameproj.db.DaoSession;
import com.lh.frameproj.db.TestDao;
import com.lh.frameproj.injector.PerApp;

import dagger.Module;
import dagger.Provides;

/**
 * Created by WE-WIN-027 on 2016/9/27.
 *
 * @des ${TODO}
 */
@Module
public class DBModule {

    @Provides @PerApp DaoMaster.DevOpenHelper provideDevOpenHelper(Context context) {
        return new DaoMaster.DevOpenHelper(context, "app.db", null);
    }

    @Provides @PerApp
    DaoMaster provideDaoMaster(DaoMaster.DevOpenHelper helper) {
        return new DaoMaster(helper.getWritableDatabase());
    }

    @Provides @PerApp
    DaoSession provideDaoSession(DaoMaster master) {
        return master.newSession();
    }

    @Provides @PerApp
    TestDao getTestDao(DaoSession session) {
        return session.getTestDao();
    }

}
