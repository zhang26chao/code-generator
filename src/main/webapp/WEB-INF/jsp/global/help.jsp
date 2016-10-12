 <%@ page language="java" pageEncoding="UTF-8" %>
<!DOCTYPE html>
<html lang="en">
<head>
</head>
<body>
<div class="panel panel-default">
    <!-- Default panel contents -->
    <div class="panel-heading">功能说明</div>
    <div class="panel-body">
        <p>
            1.模拟接口：模拟被调接口返回值。在开发中我们经常需要调用其他系统的接口，如果其他系统尚未开发完成，我们就可以使用这个功能来模拟被调用接口。
        </p>

        <p>2.接口测试：通过页面化的方式提供对ssf接口的测试功能。</p>
        <p>3.服务查询：分别在dev、sit和pre查询指定的ssf服务。查询结果包含提供服务的ResKeeper环境、服务提供方的ip地址、端口等信息。</p>
        <p>4.响应查询：用于查询异步请求返回的结果。</p>
    </div>
</div>
<div class="panel panel-default">
    <!-- Default panel contents -->
    <div class="panel-heading">模拟接口</div>
    <div class="panel-body">
        <p>
            1.在接口列表中新增一个模拟接口，输入cmdCode，比如quickpay；休眠时间模拟接口返回的延迟，如果为空，立刻返回；返回信息模拟接口返回的内容。
        </p>

        <p>2.在ssf-server.xml配置ssf mock的引用：<code>&lt;ssf:reference id="mockService"
            uri="ssf100://ssfmock.epp.suning.com/" /&gt;</code></p>

        <p>3.使用mockService进行远程调用，代码如下：
                        <pre>
        <code>
@Autowired
@Qualifier("mockService")
public GenericService genericService;

@Test
@SuppressWarnings("unchecked")
public void testRequest() {
    Map&lt;String,Object&gt; requestMap = new HashMap&lt;String, Object&gt;();
    requestMap.put("cmdCode", "quickpay"); // 保持和新增接口中的cmdCode一致
    Map&lt;String, Object&gt; responseMap = genericService.synCall("request", requestMap); // 固定值request
    System.out.println(responseMap.get("responseMsg")); // 固定值responseMsg
}
        </code>
                        </pre>
        </p>

    </div>
</div>
<div class="panel panel-default">
    <!-- Default panel contents -->
    <div class="panel-heading">接口测试</div>
    <div class="panel-body">
        <p>1.在接口测试列表界面点击<a href="<%=request.getContextPath()%>/request/create.do">新增</a>按钮，创建一个测试接口</p>
        <p>
            2.选择ssf服务器地址，目前提供dev、sit和pre三种环境。
        </p>

        <p>
            3.输入ssf地址，不可为空。
        </p>

        <p>
            4.ip地址用于确定请求发送到哪台机器。如果忽略，则优先选择本地启动的服务，如果本地服务不存在，则会在所有提供该ssf接口的机器中随机选择一个。
        </p>

        <p>
            5.输入cmdCode，必选项。
        </p>

        <p>
            6.服务类型有两种，service和notification，对应ssf接口的两种类型。如果要测试notification接口，需要在ssf-server.xml配置<code>&lt;ssf:reference
            id="mockService"
            uri="ssf100://ssfmock.epp.suning.com/" /&gt;</code>；测试service接口无需配置。
        </p>

        <p>
            7.请求模式有两种，同步和异步。同步请求会等待响应；异步请求立刻返回，响应可以在<a href="<%=request.getContextPath()%>/response/list.do">这里</a>查询。
        </p>

        <p>
            8.请求参数是发送给ssf接口的请求报文，使用添加参数按钮添加；如果参数的值是List或者Map，参数类型选择Json，参数值使用Json格式。
        </p>
        <p>
            9.输入完成后提交，页面自动回到接口列表界面，找到刚刚新增的接口，点击测试按钮即可测试，测试结果显示在列表上方。
        </p>
    </div>
</div>
<form name='sendmail' action='mailto:14080608@cnsuning.com'>
    <button type="submit" class="btn btn-primary">联系我</button>
</form>
</body>
</html>