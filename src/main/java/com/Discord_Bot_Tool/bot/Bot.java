package com.Discord_Bot_Tool.bot;

import com.Discord_Bot_Tool.db.Users;
import com.Discord_Bot_Tool.db.UsersDAO;
import com.Discord_Bot_Tool.mail.MailInfo;
import com.Discord_Bot_Tool.mail.MailSend;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.JDABuilder;
import net.dv8tion.jda.api.entities.Activity;
import net.dv8tion.jda.api.entities.MessageHistory;
import net.dv8tion.jda.api.entities.channel.concrete.TextChannel;
import net.dv8tion.jda.api.events.interaction.ModalInteractionEvent;
import net.dv8tion.jda.api.events.interaction.component.ButtonInteractionEvent;
import net.dv8tion.jda.api.events.session.ReadyEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import net.dv8tion.jda.api.interactions.components.ActionRow;
import net.dv8tion.jda.api.interactions.components.buttons.Button;
import net.dv8tion.jda.api.interactions.components.text.TextInput;
import net.dv8tion.jda.api.interactions.components.text.TextInputStyle;
import net.dv8tion.jda.api.interactions.modals.Modal;
import net.dv8tion.jda.api.requests.GatewayIntent;

import java.awt.*;
import java.time.LocalDate;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

public class Bot extends ListenerAdapter {
    private JDA jda;
    private boolean isRunning = false;

    public void JDABuild() {
        this.jda = JDABuilder.createDefault(new BotInfo().getToken())
                .enableIntents(GatewayIntent.GUILD_MEMBERS)
                .addEventListeners(this)
                .setActivity(Activity.playing(""))  // 봇 상태 메시지
                .build();

        isRunning = true;
        System.out.println("* * * * * * * * * * Bot Start Complete * * * * * * * * * *");
    }

    public void shutdown() {
        try {
            this.jda.shutdown();
            isRunning = false;
            System.out.println("* * * * * * * * * * Bot Shutdown Complete * * * * * * * * * * ");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void restart() {
        if (isRunning) {
            shutdown();
        }
        JDABuild();
    }

    @Override
    public void onReady(ReadyEvent event) { // 봇이 준비되었을 때
        TextChannel channel = event.getJDA().getTextChannelById(new BotInfo().getChannelID()); // 채널 ID

        EmbedBuilder Verify_Embed = new EmbedBuilder()
                .setTitle("기초프로젝트 7조 인증") // 제목
                .setDescription("기초프로젝트 7조 이메일 인증을 통하여 역할을 부여 받으세요.") // 설명
                .setColor(Color.decode("#006A79")) // 색상
                .setFooter("기초프로젝트 7조", event.getJDA().getSelfUser().getAvatarUrl()); // 하단에 들어가는 이미지

        if (channel != null) {
            MessageHistory history = channel.getHistory(); // 채널의 메시지 기록을 가져옵니다.
            history.retrievePast(50).queue(messages -> { // 최근 50개의 메시지를 검사합니다.
                boolean messageExists = messages.stream() // 메시지 스트림을 생성합니다.
                        .anyMatch(msg -> msg.getAuthor().equals(event.getJDA().getSelfUser()) && // 봇이 보낸 메시지인지 확인
                                !msg.getEmbeds().isEmpty()); // 임베드를 포함하는지 확인
                // 해당하는 임베드 메시지가 없다면 새로운 메시지를 보냅니다.
                if (!messageExists) {
                    channel.sendMessageEmbeds(Verify_Embed.build()).setActionRow( // 임베드를 보냅니다.
                            Button.primary("Verify_Btn", "인증하기") // 임베드에 버튼을 추가합니다.
                    ).queue();
                }
            });
        }
    }

    @Override
    public void onButtonInteraction(ButtonInteractionEvent event) { // 버튼 클릭 이벤트
        if (event.getComponentId().equals("Verify_Btn")) { // 버튼 ID가 Verify_Btn인 경우
            TextInput emailInput = TextInput.create("email_input", "이메일 주소", TextInputStyle.SHORT) // TextInput 생성
                    .setPlaceholder("example@sunmoon.ac.kr")
                    .setMinLength(0)
                    .setMaxLength(254)
                    .setRequired(true)
                    .build();

            Modal emailModal = Modal.create("email_modal", "이메일 인증") // 이메일 인증 Modal 생성
                    .addActionRows(ActionRow.of(emailInput))
                    .build();

            event.replyModal(emailModal).queue(); // Modal 전송
        } else if (event.getComponentId().equals("Verify_Input_Btn")) { // 버튼 ID가 Verify_Input_Btn인 경우
            TextInput verificationInput = TextInput.create("verification_input", "인증번호", TextInputStyle.SHORT)
                    .setPlaceholder("인증번호를 입력하세요")
                    .setMinLength(6)
                    .setMaxLength(6)
                    .setRequired(true)
                    .build();

            Modal verificationModal = Modal.create("verification_modal", "인증번호 입력") // 인증번호 입력 Modal 생성
                    .addActionRows(ActionRow.of(verificationInput))
                    .build();

            event.replyModal(verificationModal).queue(); // Modal 전송
        }
    }

    private Map<Long, String> verifyCode = new HashMap<>(); // 인증번호 HashMap 생성
    private Map<Long, String> email = new HashMap<>();  // 이메일 HashMap 생성

    @Override
    public void onModalInteraction(ModalInteractionEvent event) {   // Modal 이벤트
        if (event.getModalId().equals("email_modal")) { // 이메일 입력 Modal
            MailSend mailSend = new MailSend();
            email.put(event.getUser().getIdLong(), event.getValue("email_input").getAsString()); // 이메일 주소
            if (mailSend.isValidEmail(email.get(event.getUser().getIdLong()))) {
                // 인증번호를 보내는 동안 답변 대기
                event.deferReply().setEphemeral(true).queue();
                // 이메일 검증 성공, 인증번호 전송
                Random random = new Random();
                MailInfo mailInfo = new MailInfo();
                // 인증번호 생성 해야함
                verifyCode.put(event.getUser().getIdLong(), String.format("%06d", random.nextInt(1000000)));
                mailInfo.setContent("인증번호 " + verifyCode.get(event.getUser().getIdLong()));
                mailSend.sendMailAsync(email.get(event.getUser().getIdLong()), mailInfo)
                        .thenAcceptAsync((unused) -> {
                            // 이메일 전송 성공, 인증번호 입력 버튼 전송
                            EmbedBuilder Verify_Embed = new EmbedBuilder()
                                    .setTitle("기초프로젝트 7조 인증") // 제목
                                    .setDescription("기초프로젝트 7조 이메일 인증을 통하여 역할을 부여 받으세요.") // 설명
                                    .setColor(Color.decode("#006A79")) // 색상
                                    .setFooter("기초프로젝트 7조", event.getJDA().getSelfUser().getAvatarUrl()); // 하단에 들어가는 이미지

                            event.getHook().editOriginalEmbeds(Verify_Embed.build()) // InteractionHook을 사용하여 임베드를 수정합니다.
                                    .setActionRow(
                                            Button.primary("Verify_Input_Btn", "인증번호 입력"),
                                            Button.link("https://mail.google.com/mail/u/0/#inbox", "이메일 확인")
                                    )
                                    .queue();
                        }, event.getJDA().getRateLimitPool()) // JDA의 RateLimitPool을 사용하여 비동기로 실행
                        .exceptionally(e -> {
                            e.printStackTrace();
                            event.getHook().sendMessage("이메일을 보내는데 실패했습니다. 다시 시도해주세요.").setEphemeral(true).queue();
                            return null;
                        });
            } else {
                event.reply("이미 인증된 메일이거나, 유효하지 않은 이메일 주소입니다. 다시 시도해주세요.")  // 이메일 검증 실패
                        .setEphemeral(true)
                        .queue();
            }
            System.out.println(verifyCode);
        } else if (event.getModalId().equals("verification_modal")) { // 인증번호 입력 Modal
            String input_verifyCode = event.getValue("verification_input").getAsString(); // 인증번호

            if (verifyCode.containsKey(event.getUser().getIdLong()) && verifyCode.get(event.getUser().getIdLong()).equals(input_verifyCode)) {
                event.getGuild().addRoleToMember(event.getMember(), event.getGuild().getRoleById(new BotInfo().getRoleID())).queue(); // 역할 부여
                Users users = new Users(event.getUser().getName(),  // Users 객체 생성
                        email.get(event.getUser().getIdLong()),
                        event.getUser().getIdLong(),
                        LocalDate.now());
                new UsersDAO().Create(users);   // DB에 저장
                verifyCode.remove(event.getUser().getIdLong()); // 인증번호 삭제
                email.remove(event.getUser().getIdLong());    // 이메일 삭제
                event.reply("인증이 완료되어 역할이 부여되었습니다.").setEphemeral(true).queue();    // 인증 완료 메시지 전송
            } else {
                event.reply("인증번호가 일치하지 않습니다. 다시 시도해주세요.").setEphemeral(true).queue();  // 인증 실패 메시지 전송
            }
        }
    }
}