package com.chengm.pirate.service.mail;

import com.chengm.pirate.base.impl.BaseBizService;
import com.chengm.pirate.entity.AjaxResult;
import com.chengm.pirate.exception.InvokeException;
import com.chengm.pirate.utils.StringUtil;
import com.chengm.pirate.utils.constant.CodeConstants;
import com.chengm.pirate.utils.constant.CodeStatus;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.MailSendException;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

/**
 * program: CmPirate
 * description: 处理邮件
 * author: ChengMo
 * create: 2019-12-14 18:20
 **/
@Service
public class EmailService extends BaseBizService {

    private JavaMailSenderImpl mailSender;

    /**
     * 从配置文件中获取发件人
     */
    @Value("${spring.mail.username}")
    private String sender;

    @Autowired
    public EmailService(JavaMailSenderImpl mailSender) {
        this.mailSender = mailSender;
    }

    /**
     * 邮件发送
     * @param receiver 收件人
     * @param verCode 验证码
     * @throws MailSendException 邮件发送错误
     */
    @Async
    public void sendEmailVerCode(String receiver, String verCode) throws MailSendException {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setSubject("验证码");// 设置邮件标题
        message.setText("尊敬的用户,您好:\n"
                + "\n本次请求的邮件验证码为:" + verCode + ",本验证码10分钟内有效，请及时输入。（请勿泄露此验证码）\n"
                + "\n如非本人操作，请忽略该邮件。\n(这是一封自动发送的邮件，请不要直接回复）");	// 设置邮件正文
        message.setTo(receiver);// 设置收件人
        message.setFrom(sender);// 设置发件人
        mailSender.send(message);// 发送邮件
    }

    /**
     * 邮箱验证码验证
     */
    public int checkRegisterEmailCode(String key, String code) {
        try {
            String redisCode = (String) mRedisUtil.get(key);
            // 判断是否已过期
            if (StringUtil.isEmpty(redisCode)) {
                return CodeStatus.CODE_EXPIRE;
            }
            // 判断验证码是否正确
            if (!StringUtil.equals(redisCode, code)) {
                return CodeStatus.CODE_FAIL;
            }
            // 验证成功，从redis中移除该key&value
            mRedisUtil.setRemove(key, redisCode);
            return CodeStatus.CODE_SUCCESS;
        } catch (Exception ex) {
            throw new InvokeException(CodeConstants.SERVER_ERROR, "Server error");
        }
    }

}
