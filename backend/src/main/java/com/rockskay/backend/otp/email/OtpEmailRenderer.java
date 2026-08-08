package com.rockskay.backend.otp.email;

import org.springframework.stereotype.Component;

@Component
public class OtpEmailRenderer {

    public String render(
            OtpEmailTemplate.EmailContent content
    ) {

        long expiryMinutes = content.expiry().toMinutes();

        return """
                <!DOCTYPE html>
                <html lang="en">
                <head>
                    <meta charset="UTF-8">
                    <meta name="viewport"
                          content="width=device-width, initial-scale=1.0">
                    <title>%s</title>
                </head>

                <body style="
                    margin:0;
                    padding:0;
                    background-color:#f4f6f8;
                    font-family:Arial, Helvetica, sans-serif;
                    color:#1f2937;
                ">

                <table
                    role="presentation"
                    width="100%%"
                    cellpadding="0"
                    cellspacing="0"
                    border="0"
                    style="
                        width:100%%;
                        background-color:#f4f6f8;
                        margin:0;
                        padding:40px 16px;
                    "
                >
                    <tr>
                        <td align="center">

                            <!-- Main Container -->
                            <table
                                role="presentation"
                                width="560"
                                cellpadding="0"
                                cellspacing="0"
                                border="0"
                                style="
                                    width:100%%;
                                    max-width:560px;
                                    background-color:#ffffff;
                                    border-radius:12px;
                                    overflow:hidden;
                                    border:1px solid #e5e7eb;
                                "
                            >

                                <!-- Header -->
                                <tr>
                                    <td style="
                                        padding:28px 36px;
                                        border-bottom:1px solid #f0f0f0;
                                    ">

                                        <div style="
                                            font-size:24px;
                                            font-weight:700;
                                            letter-spacing:-0.5px;
                                            color:#111827;
                                        ">
                                            RockSkAy
                                        </div>

                                    </td>
                                </tr>

                                <!-- Content -->
                                <tr>
                                    <td style="
                                        padding:36px;
                                    ">

                                        <!-- Title -->
                                        <h1 style="
                                            margin:0 0 16px;
                                            font-size:24px;
                                            line-height:1.3;
                                            font-weight:700;
                                            color:#111827;
                                        ">
                                            %s
                                        </h1>

                                        <!-- Message -->
                                        <p style="
                                            margin:0 0 28px;
                                            font-size:15px;
                                            line-height:1.7;
                                            color:#4b5563;
                                        ">
                                            %s
                                        </p>

                                        <!-- OTP Label -->
                                        <p style="
                                            margin:0 0 10px;
                                            font-size:13px;
                                            font-weight:600;
                                            color:#6b7280;
                                            text-transform:uppercase;
                                            letter-spacing:0.8px;
                                        ">
                                            Verification code
                                        </p>

                                        <!-- OTP Box -->
                                        <table
                                            role="presentation"
                                            width="100%%"
                                            cellpadding="0"
                                            cellspacing="0"
                                            border="0"
                                        >
                                            <tr>
                                                <td align="center" style="
                                                    padding:24px 16px;
                                                    background-color:#f8fafc;
                                                    border:1px solid #e5e7eb;
                                                    border-radius:10px;
                                                ">

                                                    <span style="
                                                        font-size:32px;
                                                        line-height:1;
                                                        font-weight:700;
                                                        letter-spacing:8px;
                                                        color:#111827;
                                                    ">
                                                        %s
                                                    </span>

                                                </td>
                                            </tr>
                                        </table>

                                        <!-- Expiry -->
                                        <p style="
                                            margin:18px 0 0;
                                            font-size:14px;
                                            line-height:1.6;
                                            color:#6b7280;
                                            text-align:center;
                                        ">
                                            This code expires in
                                            <strong style="color:#374151;">
                                                %d minutes
                                            </strong>.
                                        </p>

                                        <!-- Security Notice -->
                                        <table
                                            role="presentation"
                                            width="100%%"
                                            cellpadding="0"
                                            cellspacing="0"
                                            border="0"
                                            style="margin-top:32px;"
                                        >
                                            <tr>
                                                <td style="
                                                    padding:16px;
                                                    background-color:#f9fafb;
                                                    border-radius:8px;
                                                ">

                                                    <p style="
                                                        margin:0;
                                                        font-size:13px;
                                                        line-height:1.6;
                                                        color:#6b7280;
                                                    ">
                                                        <strong style="color:#374151;">
                                                            Security notice:
                                                        </strong>
                                                        If you did not request this
                                                        code, you can safely ignore
                                                        this email. Never share your
                                                        verification code with anyone.
                                                    </p>

                                                </td>
                                            </tr>
                                        </table>

                                    </td>
                                </tr>

                                <!-- Footer -->
                                <tr>
                                    <td style="
                                        padding:22px 36px;
                                        background-color:#fafafa;
                                        border-top:1px solid #f0f0f0;
                                    ">

                                        <p style="
                                            margin:0;
                                            font-size:12px;
                                            line-height:1.6;
                                            color:#9ca3af;
                                            text-align:center;
                                        ">
                                            This is an automated email from RockSkAy.
                                            Please do not reply to this email.
                                        </p>

                                        <p style="
                                            margin:8px 0 0;
                                            font-size:12px;
                                            color:#9ca3af;
                                            text-align:center;
                                        ">
                                            © RockSkAy. All rights reserved.
                                        </p>

                                    </td>
                                </tr>

                            </table>

                        </td>
                    </tr>
                </table>

                </body>
                </html>
                """.formatted(
                content.subject(),
                content.title(),
                content.message(),
                content.otp(),
                expiryMinutes
        );
    }
}