package org.brightblock.mam.services.blockstack;

import java.util.List;

import org.brightblock.mam.services.blockstack.models.ProfileModel;
import org.brightblock.mam.services.blockstack.models.ZonefileModel;


public interface BlockstackApiService
{
	public String ping();
	public List<String> names(Integer page);
	public ZonefileModel name(String name);
	public String nameHistory(String name);
	public String nameZonefile(String name, String zoneFileHash);
	public ProfileModel profile(String name);
}
