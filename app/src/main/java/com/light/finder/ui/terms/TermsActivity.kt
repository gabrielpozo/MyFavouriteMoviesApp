package com.light.finder.ui.terms

import android.os.Bundle
import android.text.Html
import android.text.method.LinkMovementMethod
import android.view.Window
import android.view.WindowManager
import androidx.appcompat.app.AppCompatActivity
import com.light.finder.R
import kotlinx.android.synthetic.main.activity_privacy_statement.*
import kotlinx.android.synthetic.main.activity_terms.*

class TermsActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        requestWindowFeature(Window.FEATURE_NO_TITLE)
        this.window.setFlags(
            WindowManager.LayoutParams.FLAG_FULLSCREEN,
            WindowManager.LayoutParams.FLAG_FULLSCREEN
        )
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_terms)

        textViewTermsHtml.text =
            Html.fromHtml(
                """
                <p>
    Version November 2018
</p>
<p>
    Thank you for your interest in Signify LightFinder! The Signify LightFinder
    is application (“App”) is made available to you by Signify Netherlands B.V.
    having its registered office at High Tech Campus 48, 5656 AE Eindhoven, The
    Netherlands ("Signify")
    <br/>
</p>
<p>
    We welcome you to use this App, there are just a few rules and restrictions
    we ask you to keep in mind!
    <br/>
</p>
<p>
    What this App does for you:
</p>
<ul>
    <li>
        It provides you with information on Signify’s lighting products
    </li>
    <li>
        It helps you find a replacement for your current lighting products. It
        lets you upload the photo of your old lamp in order to recommend you a
        replacement
        <br/>
    </li>
</ul>
<p>
    Applicability: Please note that your use of and interaction with this App
    is subject to these Terms of Use, which remain in full force and effect so
    long as you continue to use or access the App, or until terminated earlier
    in accordance with the provisions of these Terms of Use. Any use or access
    to the App by anyone under the age of 18 is strictly prohibited and is a
    violation of these Terms of Use.
    <br/>
    <br/>
    We encourage you to read on; by using the App, you accept the terms as
    mentioned below. IF YOU DO NOT AGREE WITH THESE TERMS OF USE, YOU SHOULD
    NOT ACCESS OR USE THE APP.
    <br/>
</p>
<p>
    Scope of the App: It helps you bring the lighting products at your doorstep
    in an easy and engaging way. You can upload the photo of your old lamp and
    the App will provide you a similarly placed and you will get the
    information about where to buy such products.
    <br/>
</p>
<p>
    Privacy: Privacy of our end users is important to us, please read the
    privacy notice provided separately to you in this App carefully.
    <br/>
</p>
<p>
    Access and registration: Accessing and using the App can be done as a
    guest.
    <br/>
</p>
<p>
    License: You may use this App solely in in connection with the purpose as
    described above. The right to use this App is App is personal,
    non-exclusive, non-transferable, revocable and limited to the purpose.
    <br/>
</p>
<p>
    Restrictions: You agree to (a) not use the App in violation of any laws,
    regulation or court order, or for any unlawful or abusive purpose or in
    violation of these Terms of Use; (b) use the App only as intended by
    Signify; (c) not use the App in any manner that could harm Signify, its
    service providers, or any other person; (d) not to republish, reproduce,
    distribute, display, post or transmit any part of the App or modify or
    adapt the App or merge the App into another program or create derivative
    works based upon the App; (e) not perform an action with the intent of
    introducing to the App viruses, worms, defects, Trojan horses, malware or
    any items of a destructive nature or disabling the App or other end users’
    devices; (f) not to circumvent or attempt to tamper with the security
    settings of App; (g) not to reverse engineer, decompile, or disassemble the
    App, except to the extent that applicable law expressly prohibits the
    foregoing restriction; (h) comply with any other reasonable requirements or
    restrictions requested or imposed by Signify; (i) not publish and/or post
    any abusive, obscene or inappropriate content on the App; and (j) not
    permit others to do any of the foregoing restricted acts. You assume full
    responsibility for the legal and responsible use of the App.
    <br/>
</p>
<p>
    Third parties: It is possible that when you use this App you will (also)
    use a service, download a piece of software, or purchase goods that are
    provided by a third party. Please know that those third parties may have
    their own applicable rules and restrictions, separate from these terms of
    use.
    <br/>
</p>
<p>
    Upgrades, updates: Signify may, at its sole option, make upgrades or
    updates to the App for providing service, and may do so remotely without
    notifying you. Updates, upgrades or changes are subject to these Terms of
    Use, unless such upgrade or update is accompanied by a separate license in
    which case the terms of that license will govern. If you do not want such
    updates or upgrades, your sole remedy is to cease using the App altogether.
    Your continued use of the App implies deemed acceptance of such updates and
    upgrades by you.
    <br/>
</p>
<p>
    Support: For any questions and support with regards to the App, please
    contact 1-800-555-0050 or
    <a
        href="https://www.usa.lighting.philips.com/support/connect/contact-us/contact-us"
        target="_blank"
    >
        http://www.usa.lighting.philips.com/support/connect/contact-us/contact-us
    </a>
    <br/>
</p>
<p>
    Warranties: Our goal is to provide you with a great App and a great App
    user experience. Do know that we are only able to provide you the App
    "as-is" and we cannot warrant anything about the App or its content.
    <br/>
</p>
<p>
    Liability: As much faith as we have in our App, there is always the
    possibility that things don't work as they are supposed to. In the
    unfortunate event that the App would not work or any content may be lost,
    please accept our sincerest apologies. We certainly understand that it is
    unfortunate and inconvenient. Unfortunately, we cannot accept any liability
    for any damages incurred as a result of your use of the App. TO THE MAXIMUM
    EXTENT PERMITTED BY APPLICABLE LAW, IN ANY EVENT WE ARE NOT LIABLE FOR
    AMOUNTS EXCEEDING THE FEES PAID IN CONNECTION WITH THE APP OR FIFTY EUROS
    (€50), WHICHEVER IS HIGHER. IF ANY LIMITATION ON REMEDIES, DAMAGES OR
    LIABILITY IS PROHIBITED OR RESTRICTED BY APPLICABLE LAW, SIGNIFY SHALL
    REMAIN ENTITLED TO THE MAXIMUM DISCLAIMERS AND LIMITATIONS AVAILABLE AT
    LAW.
    <br/>
</p>
<p>
    User content: If you create or upload content on the App (“User Content”),
    you can decide what you will publicly share on our App and we want you and
    others to enjoy our App. So please don’t use our App in a way that is
    commonly considered inappropriate (such as obscene, violating laws and
    regulations, offensive, discriminatory or that infringe someone else’s
    rights).
    <br/>
</p>
<p>
    User Content is provided by users, not by Signify. We do not endorse
    opinions, recommendations, or advice expressed therein. As this is a
    publicly available App, sharing your content means that it becomes public.
    Also, when you share your content, we intend to use it for our own purposes
    as well, including without limitation for commercial purposes without any
    obligation towards you. If this is not what you would like, it is best to
    be cautious about what you share.
    <br/>
</p>
<p>
    Your indemnity: You agree to indemnify and hold harmless Signify from and
    against any damages, liabilities, costs and expenses (including reasonable
    attorneys' and professionals' fees and litigation costs) that arise out of
    or are related to the posting, content, or transmission of any message,
    data, material or any other content or information you submit on the App or
    any violation of these Terms of Use or the law by you or a third party or
    person acting on your behalf.
    <br/>
</p>
<p>
    Ownership: Copyrights and all other intellectual property rights in the App
    and the content provided by Signify, the compilation of data on the App,
    and the order, sequence and arrangement of this App, are solely owned by
    Signify Holding B.V. and / or its affiliates, partners or licensors. All
    rights are reserved. You may not use the App other than expressly permitted
    in these Terms of Use. Signify and its Affiliates or its licensors retain
    ownership of all its rights in content and in and to the App, and except as
    expressly set forth herein, no other rights or licenses are granted or
    implied to be granted under any Signify intellectual property.
    <br/>
</p>
<p>
    Amendments: These Terms of Use may be amended by Signify at any time. By
    continuing to access or use the App after such posting, you will be deemed
    to have accepted such amendments. You are advised to regularly review these
    Terms of Use and related terms and conditions, if any.
    <br/>
</p>
<p>
    Usage Data: You acknowledge and agree that Signify may collect information
    and data generated from your use of the App (“Usage Data”). Signify shall
    ensure that the use of Usage Data will exclude any data personal data.
    Signify is entitled to use the Usage Data, free of charge, at any time
    during the term of these Terms of Use and afterwards, in its sole
    discretion for any purposes including but not limited to, to aggregate or
    compile Usage Data with other data, create intellectual property rights or
    derivative works of or modify or adapt Usage Data to provide, maintain, and
    improve products and services, and to develop new products or features or
    services.
</p>
<p>
    Feedback: If you provide any ideas, suggestions, feedback or
    recommendations to Signify regarding the App ("Feedback") you acknowledge
    and agree that Signify will own all rights in Feedback including without
    limitation intellectual property rights and will be free to use such
    Feedback for any purpose whatsoever in its sole discretion without any
    further obligation or liability and without payment of royalties or other
    consideration to you.
    <br/>
</p>
<p>
    Termination: These Terms of Use shall terminate with or without notice (i)
    at the discretion of Signify, due to your failure to comply with any
    provision of these Terms of Use; or (ii) if any fee charged by Signify for
    continued use is not paid in due time; or (iii) upon destruction of your
    installed copy of the App. Upon termination of the Terms of Use you shall
    not access or use the App in any manner. Signify’s rights and your
    obligations shall survive the termination of these Terms of Use.
    <br/>
</p>
<p>
    Jurisdiction: Subject to the below paragraph and to the fullest extent
    permitted by law, these Terms of Use shall be construed, interpreted and
    governed by the laws of country of your residence without regard to
    conflicts of laws principles.
    <br/>
</p>
<p>
    If you are a user located in US, then the following section applies to you.
    To the fullest extent permitted by law, these Terms of use shall be
    construed, interpreted and governed, and the relations of the parties shall
    be determined, in accordance with the substantive laws of the State of New
    York without regard to its conflict of laws principles. The applicability
    of the United Nations Convention on Contracts for the International Sale of
    Goods, and any other laws that direct the application of the laws of any
    other jurisdiction, are expressly disclaimed and are thus excluded.
    <br/>
</p>
<p>
    Miscellaneous: You will not export or re-export any part of (the content
    of) the App. These Terms of Use shall constitute the entire agreement
    between you and Signify with respect to its subject matter. Failure by
    Signify to enforce any provision of this Agreement will not be deemed a
    waiver of future enforcement of that or any other provision. If any part of
    these Terms of Use is found invalid or unenforceable by a court of
    competent jurisdiction, then that determination will not affect the
    continued validity, legality, enforceability, and effectiveness of the
    remaining provisions of these Terms of Use.
</p>

            """.trimIndent(), Html.FROM_HTML_MODE_LEGACY
            )

        textViewTermsHtml.movementMethod = LinkMovementMethod.getInstance()
    }

}
