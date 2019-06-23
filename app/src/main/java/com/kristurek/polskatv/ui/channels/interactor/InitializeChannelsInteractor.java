package com.kristurek.polskatv.ui.channels.interactor;

import android.content.Context;

import com.kristurek.polskatv.iptv.core.IptvService;
import com.kristurek.polskatv.iptv.core.dto.ChannelsRequest;
import com.kristurek.polskatv.iptv.core.dto.ChannelsResponse;
import com.kristurek.polskatv.iptv.core.dto.common.Channel;
import com.kristurek.polskatv.iptv.core.dto.common.Group;
import com.kristurek.polskatv.iptv.core.dto.common.enumeration.ChannelType;
import com.kristurek.polskatv.service.PreferencesService;
import com.kristurek.polskatv.ui.arch.VoidParamAbstractInteractor;
import com.kristurek.polskatv.ui.channels.model.ChannelModel;
import com.kristurek.polskatv.ui.channels.model.GroupModel;
import com.kristurek.polskatv.util.DateTimeHelper;
import com.kristurek.polskatv.util.DrawableHelper;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class InitializeChannelsInteractor extends VoidParamAbstractInteractor<List<Serializable>> {

    private IptvService iptvService;
    private PreferencesService prefService;
    private Context context;

    public InitializeChannelsInteractor(Context context, IptvService iptvService, PreferencesService prefService) {
        this.iptvService = iptvService;
        this.prefService = prefService;
        this.context = context;
    }

    @Override
    protected List<Serializable> process() throws Exception {
        List<Serializable> channels = new ArrayList<>();

        Set<String> hiddenChannels = prefService.get(PreferencesService.KEYS.APPLICATION_HIDDEN_CHANNELS, new HashSet<>());
        LinkedHashMap<Integer, String> allChannels = new LinkedHashMap<>();

        ChannelsResponse response = iptvService.getChannels(new ChannelsRequest());

        for (Map.Entry<Group, List<Channel>> entry : response.getGroups().entrySet()) {
            GroupModel group = new GroupModel();
            group.setTitle(entry.getKey().getTitle().toUpperCase());

            channels.add(group);

            for (Channel channelDTO : entry.getValue()) {
                allChannels.put(channelDTO.getId(), channelDTO.getName());
                if (!hiddenChannels.contains(String.valueOf(channelDTO.getId()))) {
                    ChannelModel channel = new ChannelModel();

                    channel.setId(channelDTO.getId());
                    channel.setName(channelDTO.getName());
                    channel.setTime(DateTimeHelper.unixTimeToString(channelDTO.getLiveEpgBeginTime(), DateTimeHelper.HHmm) + " - " + DateTimeHelper.unixTimeToString(channelDTO.getLiveEpgEndTime(), DateTimeHelper.HHmm));
                    channel.setLiveEpgTitle(channelDTO.getLiveEpgTitle());
                    channel.setIcon(DrawableHelper.getIcon(context, channelDTO.getIcon()));
                    channel.setLiveEpgProgress(DateTimeHelper.currentPercentBetweenUnixTime(channelDTO.getLiveEpgBeginTime(), channelDTO.getLiveEpgEndTime()));

                    channel.setLiveEpgBeginTime(channelDTO.getLiveEpgBeginTime());
                    channel.setLiveEpgEndTime(channelDTO.getLiveEpgEndTime());
                    channel.setLiveEpgDescription(channelDTO.getLiveEpgDescription());
                    channel.setProtectedContent(channelDTO.getProtectedContent());
                    if (channelDTO.getType().equals(ChannelType.LIVE_CHANNEL))
                        channel.setType(com.kristurek.polskatv.ui.channels.model.ChannelType.LIVE_CHANNEL);
                    if (channelDTO.getType().equals(ChannelType.ARCHIVE_CHANNEL))
                        channel.setType(com.kristurek.polskatv.ui.channels.model.ChannelType.ARCHIVE_CHANNEL);

                    channels.add(channel);
                }
            }
        }

        prefService.save(PreferencesService.KEYS.APPLICATION_ALL_CHANNELS, allChannels);

        return channels;
    }
}
