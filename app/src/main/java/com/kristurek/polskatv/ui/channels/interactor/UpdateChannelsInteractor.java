package com.kristurek.polskatv.ui.channels.interactor;

import android.content.Context;

import com.kristurek.polskatv.iptv.core.IptvService;
import com.kristurek.polskatv.iptv.core.dto.CurrentEpgsRequest;
import com.kristurek.polskatv.iptv.core.dto.CurrentEpgsResponse;
import com.kristurek.polskatv.iptv.core.dto.common.Channel;
import com.kristurek.polskatv.ui.arch.SingleParamAbstractInteractor;
import com.kristurek.polskatv.ui.channels.model.ChannelModel;
import com.kristurek.polskatv.util.DateTimeHelper;
import com.kristurek.polskatv.util.DrawableHelper;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class UpdateChannelsInteractor extends SingleParamAbstractInteractor<List<Serializable>, Collection<Integer>> {

    private IptvService iptvService;
    private Context context;

    public UpdateChannelsInteractor(Context context, IptvService iptvService) {
        this.iptvService = iptvService;
        this.context = context;
    }

    @Override
    protected List<Serializable> process(Collection<Integer> param) throws Exception {
        List<Serializable> result = new ArrayList<>();

        CurrentEpgsResponse response = iptvService.getCurrentEpgs(new CurrentEpgsRequest(param));

        for (Channel channelDTO : response.getChannels()) {
            ChannelModel channel = new ChannelModel();

            channel.setId(channelDTO.getId());
            channel.setTime(DateTimeHelper.unixTimeToString(channelDTO.getLiveEpgBeginTime(), DateTimeHelper.HHmm) + " - " + DateTimeHelper.unixTimeToString(channelDTO.getLiveEpgEndTime(), DateTimeHelper.HHmm));
            channel.setLiveEpgTitle(channelDTO.getLiveEpgTitle());
            channel.setLiveEpgProgress(DateTimeHelper.currentPercentBetweenUnixTime(channelDTO.getLiveEpgBeginTime(), channelDTO.getLiveEpgEndTime()));

            channel.setLiveEpgBeginTime(channelDTO.getLiveEpgBeginTime());
            channel.setLiveEpgEndTime(channelDTO.getLiveEpgEndTime());
            channel.setLiveEpgDescription(channelDTO.getLiveEpgDescription());

            result.add(channel);
        }

        return result;
    }
}
