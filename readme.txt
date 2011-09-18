新浪微博API（java版）
============================
修改事项：
1.增加评论返回status、repleycomment字段
2.修改
statuses/friends 获取用户关注列表及每个关注用户最新一条微博
statuses/followers 获取用户粉丝列表及及每个粉丝用户最新一条微博
friends/ids 获取用户关注对象uid列表
followers/ids 获取用户粉丝对象uid列表
接口的翻页的bug，翻页处理采用cursor参数处理
3.增加user字段返回city和province信息
============================
修改事项:
1.删除basic认证的代码
2.增加话题接口相关的examples
3.修复urlencodeutil中的bug
4.user字段中增加domain和gender字段
5.userid数据类型升级为long
============================
修改事项:
1.所有支持分页的接口均加入分页支持.
2.修改一些接口传入中文参数报错的bug
3.添加upload等接口支持地理信息
4.修改ids里的id数据类型为long类型
5.增加隐私设置实体,修改并添加相关方法
6.添加Users/suggestions,friends/update_remark,emotions,trends/statuses ,trends/followtrends/destroy等接口,
7.完善weibo中方法的说明.
8.重写了examples的内容,对示例进行完善.
============================
修改事项：
1、接口返回结果采用json对象方式封装
2、修改WeiboResponse中空字符串("")调用判断的bug
3、修改了JSONObject中返回空对象的问题
4、增加了直接文件上传接口
5、修正Sdk里block接口请求地址的错误。
6、修改createAt时间格式转换的bug。
7、增加了用户标签接口、隐私设置接口、批量删除接口。
8、User/hot、statuses/reset_count以及repost_timeline和repost_by_me接口。 

常用接口实例：
（使用前先修改 Weibo.java 中 
 	public static final String CONSUMER_KEY = "";
	public static final String CONSUMER_SECRET = "";
 填写成实际申请的 CONSUMER_KEY 及 CONSUMER_SECRET）

1、获取最新公共微博列表
	参考：weibo4j.examples.GetTimelines 中的getPublicTimeline()部分

2、发表微博
	参考：weibo4j.examples.Update 中的updateStatus(...)部分

3、发表评论
	参考：weibo4j.examples.Update 中的updateComment(...)部分

4、删除评论
	参考：weibo4j.examples.Update 中的destroyComment(...)部分

5、发送私人消息
	参考：weibo4j.examples.DirectMessage 类

6、发表带图片的微博
	参考：weibo4j.examples.OAuthUploadByFile 中的uploadStatus(status,file)部分

7、更新用户头像
	参考：weibo4j.examples.OAuthUpdateProfile类

8、OAuth桌面应用访问
	步骤： 1> 在weibo4j.examples.OAuthUpdate 访问认证的URL，得到pin
		  2> 在weibo4j.examples.OAuthUpdate 	输入pin，然后得到AccessToken
		  3> 即可访问用户的微博，参考：weibo4j.examples.OAuthUpdateTwo

9、OAuth WEB应用访问
	步骤： 
					1> 在weibo4j.examples.WebOAuth 带backurl参数请求OAuth认证，获得RequestToken
				  2> 在callback 的地址里面，接受到oauth_verifier参数，然后再发一次请求，即可获得AccessToken
				  3> 用AccessToken访问用户的微博
	实例说明：
	
		在web目录下面有两个jsp页面：
		call.jsp 
				是发送request的请求，在第九行的参数“http://localhost:8080/callback.jsp”是回调地址
				  当获取成功后将RequestToken置入session，并重定向到用户认证地址
	  callback.jsp 
	  			接收到oauth_verifier参数，从session里面拿到RequestToken，再请求获取AccessToken
	    		获取到后即可对用户微博进行操作，本例中是发表微博
	
	测试环境：
		
		本例中可以将call.jsp和  callback.jsp直接放到tomcat的根目录下面：webapps\ROOT
		并将实例项目编译好的classes文件和lib目录拷贝到ROOT\WEB-INF下面
		重启tomcat，访问http://localhost:8080/call.jsp?opt=1	
	注：也可以打成war包，但注意call.jsp里面的callback参数需要做相应修改


=============================================================================
其他接口在weibo4j.Weibo类中定义，调用方式请参考weibo4j.examples，下面是完整的接口列表：

获取下行数据集(timeline)接口
statuses/public_timeline 获取最新的公共微博消息
statuses/friends_timeline 获取当前登录用户及其所关注用户的最新微博消息 (别名: statuses/home_timeline)
statuses/user_timeline 获取用户发布的微博消息列表
statuses/mentions 获取@当前用户的微博列表
statuses/comments_timeline 获取当前用户发送及收到的评论列表
statuses/comments_by_me 获取当前用户发出的评论
statuses/comments_to_me 获取当前用户收到的评论
statuses/comments 根据微博消息ID返回某条微博消息的评论列表
statuses/counts 批量获取一组微博的评论数及转发数
statuses/repost_timeline 返回一条原创微博的最新n条转发微博信息 New!
statuses/repost_by_me 返回用户转发的最新n条微博信息 New!
statuses/unread 获取当前用户未读消息数
statuses/reset_count 未读消息数清零接口
emotions 表情接口，获取表情列表

微博访问接口
statuses/show 根据ID获取单条微博信息内容
user/statuses/id 根据微博ID和用户ID跳转到单条微博页面
statuses/update 发布一条微博信息Updated!
statuses/upload 上传图片并发布一条微博信息
statuses/destroy 删除一条微博信息
statuses/repost 转发一条微博信息Updated!
statuses/comment 对一条微博信息进行评论Updated!
statuses/comment_destroy/:id 删除当前用户的微博评论信息
statuses/comment/destroy_batch 批量删除当前用户的微博评论信息
statuses/reply 回复微博评论信息Updated!

用户接口
users/show 根据用户ID获取用户资料（授权用户）
statuses/friends 获取用户关注列表及每个关注用户最新一条微博
statuses/followers 获取用户粉丝列表及及每个粉丝用户最新一条微博
users/hot 获取系统推荐用户
user/friends/update_remark更新当前登录用户所关注的某个好友的备注信息New!
users/suggestions 返回当前用户可能感兴趣的用户 Beta!

关注接口
friendships/create 关注某用户
friendships/destroy 取消关注
friendships/exists 是否关注某用户(推荐使用friendships/show)
friendships/show 获取两个用户关系的详细情况

话题接口
trends 获取某人的话题
trends/statuses 获取某一话题下的微博
trends/follow 关注某一个话题
trends/destroy 取消关注的某一个话题
trends/hourly 按小时返回热门话题
trends/daily 返回当日热门话题。
trends/weekly 返回当周热门话题。

Social Graph接口
friends/ids 获取用户关注对象uid列表
followers/ids 获取用户粉丝对象uid列表

黑名单接口 
blocks/create 将某用户加入黑名单
blocks/destroy 将某用户移出黑名单
blocks/exists 检测某用户是否是黑名单用户
blocks/blocking 列出黑名单用户(输出用户详细信息)
blocks/blocking/ids 列出分页黑名单用户（只输出id）

用户标签接口
tags 返回指定用户的标签列表
tags/create 添加用户标签
tags/suggestions 返回用户感兴趣的标签
tags/destroy 删除标签
tags/destroy_batch 批量删除标签

账号接口
account/verify_credentials 验证当前用户身份是否合法
account/rate_limit_status 获取当前用户API访问频率限制
account/end_session 当前用户退出登录
account/update_profile_image 更改头像(此接口需要申请)
account/update_profile 更改资料

收藏接口
favorites 获取当前用户的收藏列表
favorites/create 添加收藏
favorites/destroy 删除当前用户收藏的微博信息
favorites/destroy_batch 批量删除收藏的微博信息

登录/OAuth接口
oauth OAuth授权方式介绍
oauth/request_token 获取未授权的Request Token
oauth/authorize 请求用户授权Token
oauth/access_token 获取授权过的Access Token


