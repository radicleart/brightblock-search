package org.brightblock.search.service.blockstack;

import java.util.List;

import org.brightblock.search.service.blockstack.models.ProfileModel;
import org.brightblock.search.service.blockstack.models.ZonefileModel;


public interface BlockstackApiService
{
	public String ping();
	public List<String> names(Integer page);
	public ZonefileModel getZonefile(String name, boolean profile);
	public String nameHistory(String name);
	public String nameZonefile(String name, String zoneFileHash);
	public ProfileModel fetchProfileFromGaia(String name);
}
