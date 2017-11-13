package club.ranleng.psnine.data.module;

import club.ranleng.psnine.model.Topic.TopicComment;

public interface TopicCommentCallback {

    void onSuccess(TopicComment.Comment comment);

    void onFailure();
}
