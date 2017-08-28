/*************************************************************************
* Copyright (c) 2015 Lemberg Solutions
*
* Licensed under the Apache License, Version 2.0 (the "License");
* you may not use this file except in compliance with the License.
* You may obtain a copy of the License at
*
*    http://www.apache.org/licenses/LICENSE-2.0
*
* Unless required by applicable law or agreed to in writing, software
* distributed under the License is distributed on an "AS IS" BASIS,
* WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
* See the License for the specific language governing permissions and
* limitations under the License.
**************************************************************************/

package com.ls.widgets.map.providers;

import java.io.IOException;
import java.io.InputStream;

import android.content.Context;
import android.content.res.AssetManager;

import com.ls.widgets.map.config.OfflineMapConfig;
import com.ls.widgets.map.utils.OfflineMapUtil;

public class AssetTileProvider extends TileProvider
{
	private AssetManager assetManager;
	private StringBuilder sbuilder;
	
	public AssetTileProvider(Context context, OfflineMapConfig config)
	{
	    super(config);
	    
		this.assetManager = context.getAssets();
		
		sbuilder = new StringBuilder(256);
	}
	
    @Override
    protected InputStream openTile(int zoomLevel, int col, int row) throws IOException
    {
        // This is more memory efficient than regular concatenation
        String filesFolder = OfflineMapUtil.getFilesPath(config.getMapRootPath());
        sbuilder.delete(0, sbuilder.length());
        sbuilder.append(filesFolder);
        sbuilder.append(zoomLevel);
        sbuilder.append("/");
        sbuilder.append(col);
        sbuilder.append("_");
        sbuilder.append(row);
        sbuilder.append(".");
        sbuilder.append(config.getImageFormat());

        return assetManager.open(sbuilder.toString());
    }
}
