package com.light.finder.ui.terms

import android.os.Bundle
import android.text.Html
import android.view.Window
import android.view.WindowManager
import androidx.appcompat.app.AppCompatActivity
import com.light.finder.R
import kotlinx.android.synthetic.main.activity_privacy_statement.*

class PrivacyStatementActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        requestWindowFeature(Window.FEATURE_NO_TITLE)
        this.window.setFlags(
            WindowManager.LayoutParams.FLAG_FULLSCREEN,
            WindowManager.LayoutParams.FLAG_FULLSCREEN
        )
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_privacy_statement)

        textViewPrivacyHtml.text =
            Html.fromHtml(
                """
<p>
    INTRODUCTION
</p>
<p>
    Your privacy is important to Signify.
</p>
<p>
    We have drafted this Privacy Notice (also referred to as “Notice”) in an
    easy and comprehensible way in order to help you understand who we are,
    what personal data we collect about you, why we collect it, and what we do
    with it. Keep in mind that personal data (in this Notice also referred to
    as “data” or “your data”) means any information or set of information from
    which we are able, directly or indirectly, to personally identify you, in
    particular by reference to an identifier, e.g. name and surname, email
    address, phone number, etc.
</p>
<p>
    Please keep in mind that since Signify is an international company, this
    Notice may be replaced or supplemented in order to fulfill local
    requirements, as well as in order to provide you with additional
    information on how we process your data through specific Signify products,
    services, systems or applications. As specific products or services
    (including web or app-based functionality) may have specific processing of
    your data, this Notice is supplemented by our product specific notices
    which provide more specific or additional information on the processing of
    your data related to the specific product or service. Where applicable we
    publish an additional (layered) privacy notice as we want you to be
    informed and to be aware of this. You can find there
    <a href="https://www.signify.com/global/privacy/legal-information">
        additional (layered) privacy notices here
    </a>
    .
</p>
<p>
    We strongly encourage you to take some time to read this Notice in full.
</p>
<p>
    If you do not agree to this privacy notice, please do not provide us with
    your data.
</p>
<p>
    <a name="when_does_this_privacy_notice_apply"></a>
</p>
<p>
    WHEN DOES THIS PRIVACY NOTICE APPLY?<strong></strong>
</p>
<p>
    This Notice covers how we collect and use your data e.g.
</p>
<ul>
    <li>
        when you visit or use our consumer and customer-directed websites,
        applications or social media channels;
    </li>
    <li>
        purchase and use our products, services, systems or applications;
    </li>
    <li>
        subscribe to our newsletters;
    </li>
    <li>
        provide to us your goods or services;
    </li>
    <li>
        contact our customer support;
    </li>
    <li>
        join our business events;
    </li>
</ul>
<p>
    or otherwise interact with us (directly or indirectly) in your capacity as
    consumer, business customer, partner, (sub) supplier, contractor or other
    person with a business relationship with us.
</p>
<p>
    <a name="who_is_signify"></a>
</p>
<p>
    WHO IS SIGNIFY?<strong></strong>
</p>
<p>
    As Signify, we are a global organization leader in the general lighting
    market with a unique competitive position and recognized expertise in the
    development, manufacturing and application of innovative lighting products,
    systems and services.
</p>
<p>
    When this Notice mentions “we,” “us,” or the “Company,” it refers to the
    controller of your data under this Notice, namely the Signify affiliate
    with which you had, have or will have a business relationship or that
    otherwise decides which of your data are collected and how they are used,
    as well as Signify Netherlands B.V. (Registration number 17061150 - High
    Tech Campus 48, 5656 AE, Eindhoven, The Netherlands). Please note that the
    Signify affiliates include the subsidiary companies in which Signify N.V.
    has control, either through direct or indirect ownership. You may obtain a
    list of Signify affiliates by contacting the Signify Privacy Office (you
    will find the contact details in the below section “what are your
    choices?”).
</p>
<p>
    <a name="what_types_of_data_we_collect_about_you"></a>
</p>
<p>
    WHAT TYPES OF DATA WE COLLECT ABOUT YOU?<strong></strong>
</p>
<p>
    Depending on who you are (e.g. customer, consumer, supplier, business
    partner, etc.) and how you interact with us (e.g. online, offline, over the
    phone, etc.) we may process different data about you. We may collect your
    data, for example, when you visit or use our consumer and customer-directed
    websites, applications or social media channels, purchase and use our
    products, services, web-based tools, mobile applications, systems,
    subscribe to our newsletters, install a software update, provide to us your
    goods or services, contact our customer support, join our business events,
    participate to our contests, promotions and surveys or otherwise interact
    with us.
</p>
<p>
    Below you will find an overview of the categories of data that we may
    collect:
</p>
<p>
    Information you provide to us directly
</p>
<table border="1" cellspacing="0" cellpadding="0" width="632">
    <thead>
        <tr>
            <td valign="top">
                <p>
                    Categories of data
                </p>
            </td>
            <td valign="top">
                <p>
                    Examples of types of data
                </p>
            </td>
        </tr>
    </thead>
    <tbody>
        <tr>
            <td valign="top">
                <p>
                    Personal identification data
                </p>
            </td>
            <td valign="top">
                <p>
                    Name, surname, title, date of birth
                </p>
            </td>
        </tr>
        <tr>
            <td valign="top">
                <p>
                    Contact information data
                </p>
            </td>
            <td valign="top">
                <p>
                    Email, phone number, address, country
                </p>
            </td>
        </tr>
        <tr>
            <td valign="top">
                <p>
                    Account log in information
                </p>
            </td>
            <td valign="top">
                <p>
                    Log in ID, password or other security codes
                </p>
            </td>
        </tr>
        <tr>
            <td valign="top">
                <p>
                    Images and/or videos from which you may be identified
                </p>
            </td>
            <td valign="top">
                <p>
                    Pictures uploaded into Signify accounts or otherwise
                    provided to us
                </p>
            </td>
        </tr>
        <tr>
            <td valign="top">
                <p>
                    Financial data
                </p>
            </td>
            <td valign="top">
                <p>
                    Credit card data, bank account data
                </p>
            </td>
        </tr>
        <tr>
            <td valign="top">
                <p>
                    Any other information that you decide to voluntarily share
                    with Signify or its affiliates
                </p>
            </td>
            <td valign="top">
                <p>
                    Feedback, opinions, reviews, comments, uploaded files,
                    interests, information provided for our due diligence
                    process
                </p>
            </td>
        </tr>
    </tbody>
</table>
<p>
    Lastly, if you visit our premises, for security reasons we might also
    record your data through video or other electronic, digital or wireless
    surveillance system or device (e.g. CCTV).
</p>
<p>
    Information we collect automatically
</p>
<p>
    When you visit or use our websites or applications, subscribe to our
    newsletters or otherwise interact with us through our digital channels, in
    addition to the information you provide to us directly, we may collect
    information sent to us by your computer, mobile phone or other access
    device. For example, we may collect:
</p>
<table border="1" cellspacing="0" cellpadding="0" width="632">
    <thead>
        <tr>
            <td valign="top">
                <p>
                    Categories of data
                </p>
            </td>
            <td valign="top">
                <p>
                    Examples of types of data
                </p>
            </td>
        </tr>
    </thead>
    <tbody>
        <tr>
            <td valign="top">
                <p>
                    Device information
                </p>
            </td>
            <td valign="top">
                <p>
                    Hardware model, IMEI number and other unique device
                    identifiers, MAC address, IP address, operating system
                    version, settings of the device you use to access the
                    services, and device configuration
                </p>
            </td>
        </tr>
        <tr>
            <td valign="top">
                <p>
                    Log information
                </p>
            </td>
            <td valign="top">
                <p>
                    Time, duration and manner of use of our products and
                    services or products and services connected to ours
                </p>
            </td>
        </tr>
        <tr>
            <td valign="top">
                <p>
                    Location information
                </p>
            </td>
            <td valign="top">
                <p>
                    Your location (derived from your IP address, Bluetooth
                    beacons or identifiers, or other location-based
                    technologies), that may be collected when you enable
                    location-based products or features such as through our
                    apps
                </p>
            </td>
        </tr>
        <tr>
            <td valign="top">
                <p>
                    Luminaire information
                </p>
            </td>
            <td valign="top">
                <p>
                    Luminaire unique identifiers, luminaire information stored
                    in the device.
                </p>
            </td>
        </tr>
        <tr>
            <td valign="top">
                <p>
                    Other information about your use of our digital channels or
                    products
                </p>
            </td>
            <td valign="top">
                <p>
                    Naming convention of your smart home set-up, apps you use
                    or websites you visit, links you click within our
                    advertising e-mail, motion sensors data
                </p>
            </td>
        </tr>
    </tbody>
</table>
<p>
    Information we may collect from other sources
</p>
<p>
    To the extent permitted by applicable law, in addition to our websites,
    applications and other digital channels, we may also obtain information
    about you from other sources, such as public databases, joint marketing
    partners, social media platforms and other third parties. For example,
    depending on your social media settings, if you choose to connect your
    social media account to your Signify account, certain data from your social
    media account will be shared with us, which may include data that is part
    of your profile.To the extent permitted by applicable law, in addition to
    our websites, applications and other digital channels, we may also obtain
    information about you from other sources, such as public databases, joint
    marketing partners, social media platforms and other third parties. For
    example, depending on your social media settings, if you choose to connect
    your social media account to your Signify account, certain data from your
    social media account will be shared with us, which may include data that is
    part of your profile. If you interact with us through Social Network
    Services (‘SNS’) we might process your personal information in accordance
    with this Notice. What data we process will depend on what personal
    information you have provided to the SNS (such as your name, email address
    and other information you have made publicly available) when creating your
    account. Note that the data we collect from and through a SNS may depend on
    the privacy settings you have set with the SNS and the permissions you
    grant to us in connection with linking your account with our products or
    services to your account with an SNS. Your interactions with third parties
    through an SNS or similar features are governed by the respective privacy
    policies of those third parties and your agreement with the SNS. You
    acknowledge that you are entitled to use your SNS account for the purposes
    described herein without breach by you of any of the terms and conditions
    that govern the SNS.
    <br/>
</p>
<p>
    Information we may aggregate from different sources to your profile
    <br/>
</p>
<p>
    If you have indicated to us that you wish to receive personalize direct
    marketing communications, we might aggregate data from different sources
    (both internally and externally) to have a better understanding of your
    preference and interests, and be able to serve you with more relevant
    communications. You can always object to these activities by way of
    opposing to this. In particular, you can always opt-out from receiving
    marketing-related emails by following the unsubscribe instructions provided
    in each email. If you can sign-in to a Signify account, you might be given
    the option to change your communication preferences under the relevant
    section of our website or application. You can always contact us (you will
    find the contact details in the below section “what are your choices?”) to
    opt-out from receiving marketing-related communications.
</p>
<p>
    <a name="how_do_we_use_your_data"></a>
</p>
<p>
    HOW DO WE USE YOUR DATA?<strong></strong>
</p>
<p>
    We may use your data for different legitimate reasons and business
    purposes.
</p>
<p>
    Below you will find an overview of the purposes for which we may process
    your data:
</p>
<table border="1" cellspacing="0" cellpadding="0" width="632">
    <thead>
        <tr>
            <td valign="top">
                <p>
                    Purposes
                </p>
            </td>
            <td valign="top">
                <p>
                    Examples
                </p>
            </td>
        </tr>
    </thead>
    <tbody>
        <tr>
            <td valign="top">
                <p>
                    Assessment and (re)screening of (potential) Customers,
                    Suppliers and/or Business Partners
                </p>
            </td>
            <td valign="top">
                <p>
                    Conducting due diligence
                </p>
            </td>
        </tr>
        <tr>
            <td valign="top">
                <p>
                    Conclusion and execution of agreements
                </p>
            </td>
            <td valign="top">
                <p>
                    Sales, billing, shipment of products or services,
                    registration to mobile applications or websites, warranty,
                    service communications, account management
                </p>
            </td>
        </tr>
        <tr>
            <td valign="top">
                <p>
                    Providing support (upon your request)
                </p>
            </td>
            <td valign="top">
                <p>
                    Providing support via communication channels, such as
                    customer or contact center support
                </p>
            </td>
        </tr>
        <tr>
            <td valign="top">
                <p>
                    Direct marketing
                </p>
            </td>
            <td valign="top">
                <p>
                    Promoting contact with consumers and/or business customers
                    (only in certain countries), email and/or electronic
                    marketing, market surveys, personalizing your experience by
                    presenting products and offers tailored to your preference,
                    interests or profile (such as on our sites, applications or
                    in other communication channels)
                </p>
            </td>
        </tr>
        <tr>
            <td valign="top">
                <p>
                    Security and protection of our interests/assets
                </p>
            </td>
            <td valign="top">
                <p>
                    Deploying and maintaining technical and organizational
                    security measures, conducting internal audits and
                    investigations, conducting assessments to verify conflict
                    of interests
                </p>
            </td>
        </tr>
        <tr>
            <td valign="top">
                <p>
                    Compliance with legal obligations
                </p>
            </td>
            <td valign="top">
                <p>
                    Disclosing data to government institutions or supervisory
                    authorities as applicable in all countries in which we
                    operate, such as tax and national insurance deductions,
                    record-keeping and reporting obligations, conducting
                    compliance audits, compliance with government inspections
                    and other requests from government or other public
                    authorities, responding to legal process such as subpoenas,
                    pursuing legal rights and remedies, and managing any
                    internal complaints or claims
                </p>
            </td>
        </tr>
        <tr>
            <td valign="top">
                <p>
                    Defense of legal claims
                </p>
            </td>
            <td valign="top">
                <p>
                    Establishment, exercise or defense of legal claims to which
                    we are or may be subject
                </p>
            </td>
        </tr>
        <tr>
            <td valign="top">
                <p>
                    Product development
                </p>
            </td>
            <td valign="top">
                <p>
                    To improve the services, products, online services, mobile
                    applications and communications we provide towards you or
                    others
                </p>
            </td>
        </tr>
    </tbody>
</table>
<p>
    If we ask you to provide us with your data, but you chose not to, in some
    cases we will not be able to provide you with the full functionality of our
    products, services, systems or applications. Also, we might not be able to
    respond to requests you might have.
    <br/>
    <br/>
    If you decide to sell or share our products with other individuals, please
    note that certain products might store personal data relating to you or
    your use of the product. In such cases, if you intend to share, return,
    transfer ownership or possession of the device, please make sure
</p>
<p>
    (1) you perform a factory reset of the product in question. The
    instructions on how to perform this factory reset are present in the
    product manual; and
</p>
<p>
    (2) where you provide us with personal data of any other individual as part
    of your usage, you have obtained consent from that individual prior to
    providing the information to us.
</p>
<p>
    It is your responsibility to ensure that information you share with us does
    not violate any third party’s rights.
</p>
<p>
    <a name="how_do_we_use_your_data_for_marketing"></a>
</p>
<p>
    HOW DO WE USE YOUR DATA FOR MARKETING?<strong></strong>
</p>
<p>
    Signify Netherlands B.V. and other relevant Signify affiliates may send you
    regular promotional communications about their products, services, events
    and promotions.
</p>
<p>
    Such promotional communications may use, amongst others, the “PHILIPS”,
    “HUE”, “SIGNIFY” and/or “INTERACT” brand and may be sent to you via
    different channels such as: email, phone, SMS text messages, postal
    mailings, third party social networks. Please find our current list of
brands    <a href="https://www.signify.com/global/privacy/related-brands">here</a>.
    <br/>
    <br/>
    In order to provide you with the best personalized experience, sometimes
    these communications may be tailored to your preferences (for example, as
    you indicate these to us, as we may have inferred from your website visits,
    or based on the links you click in our emails, or on other information we
    might infer about you).
    <br/>
</p>
<p>
    We may also contact you regarding information, products, services, events
    and promotions from Signify as well as from other third-parties (‘our
    trusted partner’) which we believe can be relevant to you because of your
    usage of our products or in order to expand their functionality; mainly
    when we find a partner which has specific services or solutions to meet
    your needs, or to optimize your use of our products and services. We may
    contact you through electronic marketing communication channels or use
    other communication channels (such as social channels) to send you
    personalized ads and special offers. We will not share your registration
    data with our trusted partner unless we have sufficient grounds to do so.
    <br/>
</p>
<p>
    When required by applicable law, we will ask your consent before starting
    the above activities.
</p>
<p>
    Please note that even if you opt out from receiving marketing
    communications, you might still receive administrative, service and
    transaction communications from us, such as technical and/or security
    updates of our products, order confirmations, notifications about your
    account activities, and other important notices.
</p>
<p>
    <a name="on_what_legal_basis_do_we_use_your_data"></a>
</p>
<p>
    ON WHAT LEGAL BASIS DO WE USE YOUR DATA?<strong></strong>
</p>
<p>
    In order to be able to process your data, we may rely on different legal
    bases, including:
</p>
<ul>
    <li>
        Your consent (only when legally required or permitted). If we rely on
        your consent as a legal basis for processing your data, you may
        withdraw your consent at any time;
    </li>
    <li>
        The necessity to establish a contractual relationship with you and to
        perform our obligations under a contract;
    </li>
    <li>
        The necessity for us to comply with legal obligations and to establish,
        exercise, or defend our self from legal claims;
    </li>
    <li>
        The necessity to pursue our legitimate interests, including:
    </li>
</ul>
<p>
    o To ensure that our networks and information are secure
</p>
<p>
    o To administer and generally conduct business within the Company
</p>
<p>
    o To prevent or investigate suspected or actual violations of law, breaches
    of a business
    <br/>
    customer contract, or non-compliance with the Signify Integrity code or
    other Signify
    <br/>
    policies;
</p>
<p>
    o To optimize or extend our marketing reach and communication relevance;
</p>
<ul>
    <li>
        The necessity to respond to your requests;
    </li>
    <li>
        The necessity to protect the vital interests of any person;
    </li>
    <li>
        Any other legal basis anyhow permitted by local laws.
    </li>
</ul>
<p>
    <a name="when_do_we_share_your_data"></a>
</p>
<p>
    WHEN DO WE SHARE YOUR DATA?<strong></strong>
</p>
<p>
    We do not share any of your data except in the limited cases described
    here.
</p>
<p>
    If it is necessary for the fulfillment of the purposes described in this
    Notice, we may disclose your data to the following entities:
</p>
<ul>
    <li>
        Signify affiliates: due to our global nature, your data may be shared
        with certain Signify affiliates. Access to your data within Signify
        will be granted on a need-to-know basis;
    </li>
    <li>
        Service providers: like many businesses, we may outsource certain data
        processing activities to trusted third party service providers to
        perform functions and provide services to us, such as ICT service
        providers, consulting providers, shipping providers, payment providers,
        electronic communication service platforms;
    </li>
    <li>
        Business partners: we may share your data with trusted business
        partners so they can provide you with the services you request;
    </li>
    <li>
        Public and governmental authorities: when required by law, or as
        necessary to protect our rights, we may share your data with entities
        that regulate or have jurisdiction over Signify.
    </li>
    <li>
        Professional advisors and others: we may share your data with other
        parties including professional advisors, such as banks, insurance
        companies, auditors, lawyers, accountants, other professional advisors.
    </li>
    <li>
        Other parties in connection with corporate transactions: we may also,
        from time to time, share your data in the course of corporate
        transactions, such as during a sale of a business or a part of a
        business to another company, or any reorganization, merger, joint
        venture, or other disposition of our business, assets, or stock
        (including in connection with any bankruptcy or similar proceeding).
    </li>
    <li>
        Upon your request in case of a personal data portability request.
    </li>
</ul>
<p>
    <a name="when_do_we_transfer_your_data_abroad"></a>
</p>
<p>
    WHEN DO WE TRANSFER YOUR DATA ABROAD?<strong></strong>
</p>
<p>
    Due to our global nature, data you provide to us may be transferred to or
    accessed by Signify affiliates and trusted third parties from many
    countries around the world. As a result, your data may be processed outside
    the country where you live, if this is necessary for the fulfillment of the
    purposes described in this Notice.
</p>
<p>
    If you are located in a country member of the European Economic Area, we
    may transfer your data to countries located outside of the European
    Economic Area. Some of these countries are recognized by the European
    Commission as providing an adequate level of protection. With regard to
    transfers from the European Economic Area to other countries that are not
    are recognized by the European Commission as providing an adequate level of
    protection, we have put in place adequate measures to protect your data,
    such as organizational and legal measures (e.g. binding corporate rules and
    approved European Commission standard contractual clauses). You may obtain
    a copy of these measures by contacting the Signify Privacy Office (you will
    find the contact details in the below section “what are your choices?”).
</p>
<p>
    <a name="how_long_do_we_keep_your_data"></a>
</p>
<p>
    HOW LONG DO WE KEEP YOUR DATA?<strong></strong>
</p>
<p>
    We keep your data for the period necessary to fulfill the purposes for
    which it has been collected (for details on these purposes, see above
    section “How do we use your data?”). Please keep in mind that in certain
    cases a longer retention period may be required or permitted by law. The
    criteria used to determine our retention periods include:
</p>
<ul>
    <li>
        How long is the data needed to provide you with our products or
        services or to operate our business?
    </li>
    <li>
        Do you have an account with us? In this case, we will keep your data
        while your account is active or for as long as needed to provide the
        services to you.
    </li>
    <li>
        Are we subject to a legal, contractual, or similar obligation to retain
        your data? Examples can include mandatory data retention laws in the
        applicable jurisdiction, government orders to preserve data relevant to
        an investigation, or data that must be retained for the purposes of
        litigation, or protection against a possible claim.
    </li>
</ul>
<p>
    <a name="how_do_we_secure_your_data"></a>
</p>
<p>
    HOW DO WE SECURE YOUR DATA?<strong></strong>
</p>
<p>
    To protect your data, we will take appropriate measures that are consistent
    with applicable data protection and data security laws and regulations,
    including requiring our service providers to use appropriate measures to
    protect the confidentiality and security of your data. Unfortunately, no
    data transmission or storage system can be guaranteed to be 100% secure.
    Depending on the state of the art, the costs of the implementation and the
    nature of the data to be protected, we put in place technical and
    organizational measures to prevent risks such as destruction, loss,
    alteration, unauthorized disclosure of, or access to your data. If you have
    reason to think that your interaction with us or your Data is no longer
    processed in a secure manner, please reach out to the Signify Privacy
    Office immediately in accordance with “WHAT ARE YOUR CHOICES?” as described
    below.
</p>
<p>
    <a name="what_are_your_responsibilities"></a>
</p>
<p>
    WHAT ARE YOUR RESPONSIBILITIES?<strong></strong>
</p>
<p>
    We would like to remind you that it is your responsibility to ensure, to
    the best of your knowledge, that the data you provide us with, are
    accurate, complete and up-to-date. Furthermore, if you share with us data
    of other people, it is your responsibility to collect such data in
    compliance with local legal requirements. For instance, you should inform
    such other people, whose data you provide to us, about the content of this
    Notice and obtain their prior consent.
</p>
<p>
    <a name="what_are_your_choices"></a>
</p>
<p>
    WHAT ARE YOUR CHOICES?<strong></strong>
</p>
<p>
    We aim to provide you with access to your data. Usually you can
    autonomously control your data (e.g. by logging in to your account) and
    update, modify or, if legally possible, delete it. In this case, we
    strongly encourage you to take control of your data.
</p>
<p>
    You can always contact our Privacy Office if you would like to:
</p>
<ul>
    <li>
        review, change or delete the data you have supplied us with (to the
        extent Signify is not otherwise permitted or required to keep such
        data);
    </li>
    <li>
        object to certain data processing operations (e.g., opt-out from
        marketing communications);
    </li>
    <li>
        receive a copy of your data (in a common machine readable format, to
        the extent it is required by applicable law);
    </li>
    <li>
        ask us any other questions related to the protection of your data in
        Signify
    </li>
</ul>
<p>
    For any questions or reasonable inquiry related to the protection of your
    data in Signify or regarding this Notice in general, you can contact the
    Signify Privacy Office:
</p>
<p>
    The contact details of our Privacy Office are:
</p>
<ul>
    <li>
        Mail: Signify - Attn: Privacy Office – Herikerbergweg 102, 1101CM
        Amsterdam Zuid Oost, The Netherlands; or
    </li>
    <li>
        Online:
        <a href="https://www.signify.com/global/privacy">
            Signify Privacy Center
        </a>
        , “Privacy request” section.
    </li>
</ul>
<p>
    Please keep in mind that email communications are not always secure.
    Therefore, please do not include sensitive information in your emails to
    us.
    <br/>
</p>
<p>
    We will do our best to address your request in time and free of charge,
    except where it would require a disproportionate effort. In certain cases,
    we may ask you to verify your identity before we can act on your request.
    If you are unsatisfied with the reply received, you may then refer your
    complaint to the relevant regulator in your jurisdiction.
</p>
<p>
    <a name="how_do_we_use_cookies_and_other_tracking"></a>
</p>
<p>
    HOW DO WE USE COOKIES AND OTHER TRACKING TECHNOLOGIES?<strong></strong>
</p>
<p>
    For more information on how we use cookies and other tracking technologies,
read our Cookie Notice (which can be found in the website of the    <a href="https://www.signify.com/global/privacy">Signify Privacy Center</a>
    , see “Legal information” section).
</p>
<p>
    <a name="do_we_collect_data_from_children"></a>
</p>
<p>
    DO WE COLLECT DATA FROM CHILDREN?<strong></strong>
</p>
<p>
    We do not intentionally collect information from children under the age of
    16.
</p>
<ul>
    <li>
        Special note to Children under the age of 16: if you are under the age
        of 16, we advise that you speak with and get your parent or guardian’s
        consent before sharing your data with us;
    </li>
    <li>
        Special note to Parents of Children under the age of 16: we recommend
        you to check and monitor your children's use of our products, systems,
        services, applications (including websites and other digital channels)
        in order to make sure that your child does not share personal data with
        us without asking your permission.
    </li>
</ul>
<p>
    <a name="california_privacy_disclosures"></a>
</p>
<p>
    CALIFORNIA PRIVACY DISCLOSURES<strong></strong>
</p>
<p>
    The California Consumer Privacy Act (‘CCPA’) provides California consumer
    residents with specific rights regarding their personal information. In
    additional to the above this section describes your CCPA rights and
    explains how to exercise those rights.
</p>
<p>
    The following is not applicable to de-identified or aggregated personal
    information or data publicly available.
</p>
<p>
    <a name="information_we_collect"></a>
</p>
<p>
    INFORMATION WE COLLECT<strong></strong>
</p>
<p>
    In the execution of our services and/or when visiting our Website we
    collect information that identifies, relates to, describes, references, is
    capable of being associated with, or could reasonably be linked, directly
    or indirectly, with a particular consumer or device (defined as “data”). In
    particular, we have collected the following categories of data from our
    consumers within the last twelve (12) months:
</p>
<table border="1" cellspacing="0" cellpadding="0" width="632">
    <thead>
        <tr>
            <td valign="top">
                <p>
                    Category
                </p>
            </td>
            <td valign="top">
                <p>
                    Examples
                </p>
            </td>
            <td valign="top">
                <p>
                    Collected
                </p>
            </td>
        </tr>
    </thead>
    <tbody>
        <tr>
            <td valign="top">
                <p>
                    A. Identifiers.
                </p>
            </td>
            <td valign="top">
                <p>
                    A real name, alias, postal address, unique personal
                    identifier, online identifier, Internet Protocol address,
                    email address, account name, Social Security number,
                    driver’s license number, passport number, or other similar
                    identifiers.
                </p>
            </td>
            <td valign="top">
                <p>
                    YES
                </p>
            </td>
        </tr>
        <tr>
            <td valign="top">
                <p>
                    B.Personal information categories listed in the California
                    Customer Records statute (Cal. Civ. Code § 1798.80(e)).
                </p>
            </td>
            <td valign="top">
                <p>
                    A name, signature, Social Security number, physical
                    characteristics or description, address, telephone number,
                    passport number, driver’s license or state identification
                    card number, insurance policy number, education,
                    employment, employment history, bank account number, credit
                    card number, debit card number, or any other financial
                    information, medical information, or health insurance
                    information.
                </p>
                <p>
                    Some personal information included in this category may
                    overlap with other categories.
                </p>
            </td>
            <td valign="top">
                <p>
                    YES
                </p>
            </td>
        </tr>
        <tr>
            <td valign="top">
                <p>
                    C.Protected classification characteristics under California
                    or federal law.
                </p>
            </td>
            <td valign="top">
                <p>
                    Age (40 years or older), race, color, ancestry, national
                    origin, citizenship, religion or creed, marital status,
                    medical condition, physical or mental disability, sex
                    (including gender, gender identity, gender expression,
                    pregnancy or childbirth and related medical conditions),
                    sexual orientation, veteran or military status, genetic
                    information (including familial genetic information).
                </p>
            </td>
            <td valign="top">
                <p>
                    YES
                </p>
            </td>
        </tr>
        <tr>
            <td valign="top">
                <p>
                    D.Commercial information.
                </p>
            </td>
            <td valign="top">
                <p>
                    Records of personal property, products or services
                    purchased, obtained, or considered, or other purchasing or
                    consuming histories or tendencies.
                </p>
            </td>
            <td valign="top">
                <p>
                    YES
                </p>
            </td>
        </tr>
        <tr>
            <td valign="top">
                <p>
                    E.Biometric information.
                </p>
            </td>
            <td valign="top">
                <p>
                    Genetic, physiological, behavioral, and biological
                    characteristics, or activity patterns used to extract a
                    template or other identifier or identifying information,
                    such as, fingerprints, faceprints, and voiceprints, iris or
                    retina scans, keystroke, gait, or other physical patterns,
                    and sleep, health, or exercise data.
                </p>
            </td>
            <td valign="top">
                <p>
                    NO
                </p>
            </td>
        </tr>
        <tr>
            <td valign="top">
                <p>
                    F. Internet or other similar network activity.
                </p>
            </td>
            <td valign="top">
                <p>
                    Browsing history, search history, information on a
                    consumer’s interaction with a website, application, or
                    advertisement.
                </p>
            </td>
            <td valign="top">
                <p>
                    YES
                </p>
            </td>
        </tr>
        <tr>
            <td valign="top">
                <p>
                    G. Geolocation data.
                </p>
            </td>
            <td valign="top">
                <p>
                    Physical location or movements.
                </p>
            </td>
            <td valign="top">
                <p>
                    YES
                </p>
            </td>
        </tr>
        <tr>
            <td valign="top">
                <p>
                    H. Sensory data.
                </p>
            </td>
            <td valign="top">
                <p>
                    Audio, electronic, visual, thermal, olfactory, or similar
                    information.
                </p>
            </td>
            <td valign="top">
                <p>
                    YES
                </p>
            </td>
        </tr>
        <tr>
            <td valign="top">
                <p>
                    I. Professional or employment-related information.
                </p>
            </td>
            <td valign="top">
                <p>
                    Current or past job history or performance evaluations.
                </p>
            </td>
            <td valign="top">
                <p>
                    NO
                </p>
            </td>
        </tr>
        <tr>
            <td valign="top">
                <p>
                    J. Non-public education information (per the Family
                    Educational Rights and Privacy Act (20 U.S.C. Section
                    1232g, 34 C.F.R. Part 99)).
                </p>
            </td>
            <td valign="top">
                <p>
                    Education records directly related to a student maintained
                    by an educational institution or party acting on its
                    behalf, such as grades, transcripts, class lists, student
                    schedules, student identification codes, student financial
                    information, or student disciplinary records.
                </p>
            </td>
            <td valign="top">
                <p>
                    NO
                </p>
            </td>
        </tr>
        <tr>
            <td valign="top">
                <p>
                    K. Inferences drawn from other personal information.
                </p>
            </td>
            <td valign="top">
                <p>
                    Profile reflecting a person’s preferences, characteristics,
                    psychological trends, predispositions, behavior, attitudes,
                    intelligence, abilities, and aptitudes.
                </p>
            </td>
            <td valign="top">
                <p>
                    YES
                </p>
            </td>
        </tr>
    </tbody>
</table>
<p>
    We disclose your data for a business purpose to the following categories of
    third parties:
</p>
<ul>
    <li>
        Signify Affiliates.
    </li>
    <li>
        Service providers.
    </li>
    <li>
        Third parties to whom you or your agents authorize us to disclose your
        personal information in connection with products or services we provide
        to you.
    </li>
</ul>
<p>
    <a name="data_from_children"></a>
</p>
<p>
    DATA FROM CHILDREN<strong></strong>
</p>
<p>
    We do not intentionally collect information from children under the age of
    sixteen (16) years as we do not offer services to them. Parent who set up a
    profile holding information about children under the age of sixteen (16)
    years can only do so by granting parental consent which consent choices can
    be changed by the adults in the family. We do not direct services to
    children under the age of thirteen (13) years old. As a result, thereof we
    do not knowingly process data or information from children under thirteen
    13 years old.
</p>
<p>
    <a name="do_not_sell"></a>
</p>
<p>
    DO NOT SELL?<strong></strong>
</p>
<p>
    We do not respond to “Do Not Sell” requests as we do not track your data
    across third party websites, nor do we sell your data to third parties to
    provide targeted advertising. Nonetheless, we aim to make your online
    experience and interaction with our websites as informative, relevant and
    supportive as possible. One way of achieving this is to use cookies or
    similar techniques, which store information about your visit to our site on
    your computer. For more information on how we use cookies and other
    tracking technologies, read our Cookie Notice (see section “How do we use
    cookies and other tracking technologies”)
    <br/>
</p>
<p>
    Your Rights?
</p>
<p>
    “Right to Know”: You have the right to request that we disclose to you what
    personal information of yours that we collect, use, and/or disclose.
</p>
<p>
    “Right to Delete:” You have the right to request the deletion of your
    personal information collected or maintained by us.
    <br/>
</p>
<p>
    If you have questions about the foregoing or how to execute your Right to
    Know and/or Right to Delete in our Privacy Notice or specifically with
    regard to categories and specific pieces of data processed, categories of
    sources from which your data is collected by us, categories of third
    parties with whom we share your data in the preceding 12 months of its
    collection and the business or commercial purpose for collecting please
    contact our Privacy Office (see section “What are your choices?”). You may
    also make a verifiable request to exercise your rights by contacting us via
    the toll-free telephone 1-800-555-0050.
    <br/>
</p>
<p>
    If we cannot verify if you are the California consumer making the request
    is the consumer about whom we have collected information (or is authorized
    to act on your behalf) we have the right to deny requests. While verifying
    your identity we shall generally avoid requesting additional information
    from you for purposes of verification. If, however, we cannot verify your
    identity from the information already maintained by us, we may request
    additional information from you, which shall only be used for the purposes
    of verifying your identity while you are seeking to exercise your rights
    under the CCPA, and for security or fraud-prevention purposes. We shall
    delete any new personal information collected for the purposes of
    verification as soon as practical after processing your request, except as
    required to comply with applicable legislation.
    <br/>
</p>
<p>
    An “Authorized agent” means a natural person or a business entity
    registered with the Secretary of State that a consumer has authorized to
    act on your behalf and conditioned you have
    <br/>
</p>
<p>
    1. Provided the authorized agent written permission to do so and we could
    verify this; and
</p>
<p>
    2, Verified your own identity directly with the business.
    <br/>
</p>
<p>
    Subsection 1 does not apply when you have provided the authorized agent
    with a valid power of attorney.
    <br/>
</p>
<p>
    We endeavor to timely respond to a verifiable individual, free of charge
    and in a portable format unless it is excessive, repetitive or material
    unfounded. If we require more time, we will inform you of the reason
    thereof and extension period in writing.
    <br/>
</p>
<p>
    Non-Discrimination
</p>
<p>
    We will not discriminate against you for exercising your rights under the
    CCPA. Unless permitted by applicable regulations, we will not charge
    different prices or rates, deny products or services, provide different
    products or services or level of quality and/or suggest you may receive the
    foregoing mentioned.
    <br/>
</p>
<p>
    However, we may offer you certain financial incentives permitted by the
    CCPA that can result in different prices, rates, or quality levels. Any
    CCPA-permitted financial incentive we offer will reasonably relate to your
    data’s value and contain written terms that describe the program’s material
    aspects. Participation in a financial incentive program requires your prior
    opt in consent, which you may revoke at any time.
</p>
<p>
    <a name="when_will_there_be_updates_to_this_priva"></a>
</p>
<p>
    WHEN WILL THERE BE UPDATES TO THIS PRIVACY NOTICE?<strong></strong>
</p>
<p>
    This privacy notice might change from to time.
</p>
<p>
    The most current version of this Notice will govern our use of your data
and can be found in the website of the Signify    <a href="https://www.signify.com/global/privacy"> Privacy Center</a>, see
    “Legal information” section.
</p>
<p>
    - Signify -
</p>

            """.trimIndent(), Html.FROM_HTML_MODE_LEGACY
            )
    }
}
