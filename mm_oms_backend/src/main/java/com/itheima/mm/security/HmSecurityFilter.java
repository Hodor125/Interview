package com.itheima.mm.security;

import com.itheima.framework.annotation.HmClassScanner;
import com.itheima.framework.annotation.HmComponent;
import com.itheima.framework.annotation.HmRequestMapping;
import com.itheima.mm.common.GlobalConst;
import com.itheima.mm.pojo.User;
import lombok.extern.slf4j.Slf4j;
import org.dom4j.Attribute;
import org.dom4j.Document;
import org.dom4j.Element;
import org.dom4j.Node;
import org.dom4j.io.SAXReader;

import javax.servlet.*;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.itheima.mm.common.GlobalConst.SESSION_KEY_USER;

/**
 * 权限过滤器
 */
@Slf4j
public class HmSecurityFilter implements Filter {
    private Map<String,String> accessPathAuthMap = new HashMap<>();
    private String basePackage;

    public void destroy() {
        log.info("SecurityFilterDestroy...");
    }

    public void doFilter(ServletRequest req, ServletResponse resp, FilterChain chain) throws ServletException, IOException {
        log.info("HmSecurityFilter-doFilter...");
        HttpServletRequest request = (HttpServletRequest) req;
        HttpServletResponse response = (HttpServletResponse) resp;

        String servletPath = request.getServletPath();
        String contextPath = request.getContextPath();
        if(servletPath.endsWith(".do")){
            servletPath = servletPath.replaceAll(".do","");
        }
        //   servletPath:/course/findByListByPage.do,contextPath:/mm,requestURI:/mm/course/findByListByPage.do
        log.info("###servletPath:{},contextPath:{}###", servletPath,contextPath);
        //访问的路径不在容器中直接放行
        if(!accessPathAuthMap.containsKey(servletPath)){
            log.info("该资源可放行...");
            chain.doFilter(request,response);
            //必须返回，否则出错
            return;
        }
        String accessAuths = accessPathAuthMap.get(servletPath);
        log.debug("###当前访问路径:{},需要的权限:{}###",servletPath,accessAuths);
        //获取session会话信息，如果session为null则直接重定向到登录页面
        //false表示如果没有session不必创建 session
        HttpSession session = request.getSession(false);
        if(session == null || session.getAttribute(SESSION_KEY_USER) == null){
            response.sendRedirect("http://localhost:8080/mm/login.html");
            return;
        }
        String[] auths = accessAuths.split(",");
        User user = (User) session.getAttribute(SESSION_KEY_USER);
        List<String> authorityList = user.getAuthorityList();
        log.info("当前用户的权限:{}",authorityList);
        boolean isAuth = false;
        for (String auth : auths) {
            if(authorityList.contains(auth)){
                isAuth = true;
                break;
            }
        }
        if(isAuth){
            log.info("当前用户权限匹配，放行...");
            chain.doFilter(request,response);
        } else {
            response.getWriter().write("当前用户权限不足，请切换用户");
        }
    }

    public void init(FilterConfig config) throws ServletException {
        log.info("SecurityFilterInit...");
        //读取过滤器的初始化参数
        String filePath = config.getInitParameter(HmSecurityConst.CONFIG_SECURITYCONFIGLOCATION);
        //初始化工作，解析xml，提取资源映射关系
        log.info("Security-parseXML...");
        parseSecurityXml(filePath);
        //解析自定义组件，提取资源映射关系
        parseAnno();
    }

    /**
     * 解析xml并将值存入map
     * @param filePath
     */
    private void parseSecurityXml(String filePath){
        //获取文件输入流,之前了解的是读取src目录下的资源文件？
        InputStream resourceAsStream = this.getClass().getResourceAsStream(filePath);
        try{
            //使用工具类解析xml
            SAXReader saxReader = new SAXReader();
            //传入文件流
            Document document = saxReader.read(resourceAsStream);
            //使用Xpath语法解析，选取叶子节点
            //双斜杠//表示	从匹配选择的当前节点选择文档中的节点，而不考虑它们的位置。
            //选取所有的security元素
            List<Node> nodeList = document.selectNodes("//" + HmSecurityConst.TAG_SECURITY);
            //遍历元素获取属性
            for (Node node : nodeList) {
                //因为都是叶子节点，强转为element
                Element element = (Element) node;
                String accessPath = element.attribute(HmSecurityConst.TAG_SECURITY_ATTR_PATTERN).getStringValue();
                String accessRoles = element.attribute(HmSecurityConst.TAG_SECURITY_ATTR_HAS_ROLE).getStringValue();
                log.info("accessPath:{},accessRoles:{}",accessPath,accessRoles);
                accessPathAuthMap.put(accessPath,accessRoles);
            }

            //读取scan
            Node node = document.selectSingleNode("//" + HmSecurityConst.TAG_SCAN);
            Element element = (Element) node;
            basePackage = element.attribute(HmSecurityConst.TAG_SCAN__PACKAGE).getStringValue();
        } catch (Exception e){
            e.printStackTrace();
        }
    }

    private void parseAnno(){
        //使用工具类扫包
        List<Class<?>> classsFromPackage = HmClassScanner.getClasssFromPackage(basePackage);
        //遍历选取带HmComponent注解的
        for (Class<?> aClass : classsFromPackage) {
            if(aClass.isAnnotationPresent(HmComponent.class)){
                Method[] methods = aClass.getMethods();
                for (Method method : methods) {
                    if(method.isAnnotationPresent(HmAuthority.class)){
                        String accessPath = method.getAnnotation(HmRequestMapping.class).value();
                        String authority = method.getAnnotation(HmAuthority.class).value();
                        //在doFilter中被使用了
                        accessPathAuthMap.put(accessPath,authority);
                    }
                }
            }
        }
        log.info("accessPathAuthMap:{}",accessPathAuthMap);
    }
}
