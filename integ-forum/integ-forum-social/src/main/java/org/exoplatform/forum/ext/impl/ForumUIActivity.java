package org.exoplatform.forum.ext.impl;

import org.apache.commons.lang.StringUtils;
import org.exoplatform.forum.service.Utils;
import org.exoplatform.services.log.ExoLogger;
import org.exoplatform.services.log.Log;
import org.exoplatform.social.webui.activity.BaseUIActivity;
import org.exoplatform.webui.config.annotation.ComponentConfig;
import org.exoplatform.webui.config.annotation.EventConfig;
import org.exoplatform.webui.core.lifecycle.UIFormLifecycle;
import org.exoplatform.webui.core.lifecycle.WebuiBindingContext;

@ComponentConfig(lifecycle = UIFormLifecycle.class, template = "classpath:groovy/ks/social-integration/plugin/space/ForumUIActivity.gtmpl", events = {
    @EventConfig(listeners = BaseUIActivity.ToggleDisplayLikesActionListener.class),
    @EventConfig(listeners = BaseUIActivity.ToggleDisplayCommentFormActionListener.class),
    @EventConfig(listeners = BaseUIActivity.LikeActivityActionListener.class),
    @EventConfig(listeners = BaseUIActivity.SetCommentListStatusActionListener.class),
    @EventConfig(listeners = BaseUIActivity.PostCommentActionListener.class),
    @EventConfig(listeners = BaseUIActivity.DeleteActivityActionListener.class, confirm = "UIActivity.msg.Are_You_Sure_To_Delete_This_Activity"),
    @EventConfig(listeners = BaseUIActivity.DeleteCommentActionListener.class, confirm = "UIActivity.msg.Are_You_Sure_To_Delete_This_Comment") })
public class ForumUIActivity extends BaseKSActivity {

  private static final Log LOG = ExoLogger.getLogger(ForumUIActivity.class);

  public ForumUIActivity() {
  }

  /*
   * used by template, see line 201 ForumUIActivity.gtmpl
   */
  @SuppressWarnings("unused")
  private String getReplyLink() {
    StringBuffer sb = new StringBuffer(getViewLink());
    if (sb.lastIndexOf("/") == -1 || sb.lastIndexOf("/") != sb.length() - 1) {
      sb.append("/");
    }
    // add signal to show reply form
    sb.append("false");
    return sb.toString();
  }

  private String getViewLink() {
    String link = "";
    if (getActivityParamValue(ForumSpaceActivityPublisher.ACTIVITY_TYPE_KEY).toLowerCase()
                                                                            .indexOf("topic") >= 0) {
      link = getActivityParamValue(ForumSpaceActivityPublisher.TOPIC_LINK_KEY);
    } else {
      link = getActivityParamValue(ForumSpaceActivityPublisher.POST_LINK_KEY) + "/"
          + getActivityParamValue(ForumSpaceActivityPublisher.POST_ID_KEY);
    }
    return link;
  }

  private String getLink(String tagLink, String nameLink) {
    tagLink = StringUtils.replace(tagLink, "{0}", getViewLink());
    return StringUtils.replace(tagLink, "{1}", nameLink);
  }

  /*
   * used by Template, line 160 ForumUIActivity.gtmpl
   */
  @SuppressWarnings("unused")
  private String getActivityContentTitle(WebuiBindingContext _ctx, String herf) throws Exception {
    String title = "", linkTag = "";
    try {
      if (getActivityParamValue(ForumSpaceActivityPublisher.ACTIVITY_TYPE_KEY).equalsIgnoreCase(ForumSpaceActivityPublisher.ACTIVITYTYPE.AddPost.toString())) {
        title = _ctx.appRes("ForumUIActivity.label.add-post");
        linkTag = getLink(herf, getActivityParamValue(ForumSpaceActivityPublisher.POST_NAME_KEY));
      } else if (getActivityParamValue(ForumSpaceActivityPublisher.ACTIVITY_TYPE_KEY).equalsIgnoreCase(ForumSpaceActivityPublisher.ACTIVITYTYPE.UpdatePost.toString())) {
        title = _ctx.appRes("ForumUIActivity.label.update-post");
        linkTag = getLink(herf, getActivityParamValue(ForumSpaceActivityPublisher.POST_NAME_KEY));
      } else if (getActivityParamValue(ForumSpaceActivityPublisher.ACTIVITY_TYPE_KEY).equalsIgnoreCase(ForumSpaceActivityPublisher.ACTIVITYTYPE.AddTopic.toString())) {
        title = _ctx.appRes("ForumUIActivity.label.add-topic");
        linkTag = getLink(herf, getActivityParamValue(ForumSpaceActivityPublisher.TOPIC_NAME_KEY));
      } else if (getActivityParamValue(ForumSpaceActivityPublisher.ACTIVITY_TYPE_KEY).equalsIgnoreCase(ForumSpaceActivityPublisher.ACTIVITYTYPE.UpdateTopic.toString())) {
        title = _ctx.appRes("ForumUIActivity.label.update-topic");
        linkTag = getLink(herf, getActivityParamValue(ForumSpaceActivityPublisher.TOPIC_NAME_KEY));
      }
    } catch (Exception e) { // WebUIBindingContext
      LOG.debug("Failed to get activity content and title ", e);
    }
    if (!Utils.isEmpty(title)) {
      title = StringUtils.replace(title, "{0}", getUriOfAuthor());
      title = StringUtils.replace(title, "{1}", linkTag);
    }
    return title;
  }

}
