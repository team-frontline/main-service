package com.frontline.mainservice.utils;

import com.frontline.mainservice.config.Config;
import org.bouncycastle.asn1.x500.RDN;
import org.bouncycastle.asn1.x500.X500Name;
import org.bouncycastle.asn1.x500.style.BCStyle;
import org.bouncycastle.cert.jcajce.JcaX509CertificateHolder;
import org.bouncycastle.util.io.pem.PemObject;
import org.bouncycastle.util.io.pem.PemWriter;
import org.frontline.certInstaller.utils.KeyOperator;
import org.springframework.http.HttpHeaders;

import java.io.*;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.cert.CertificateEncodingException;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import java.util.List;
import java.util.logging.Logger;

public class CertX509Handler {
    final static Logger logger = Logger.getLogger(CertX509Handler.class.toString());

    public static String getDomainName(X509Certificate certificate) throws CertificateEncodingException {
        X500Name x500name = new JcaX509CertificateHolder(certificate).getSubject();
        RDN cn = x500name.getRDNs(BCStyle.CN)[0];
        String commonName = cn.getFirst().getValue().toString();
        return commonName;
    }

    public static String stringFromCert(X509Certificate certificate) throws IOException, CertificateEncodingException {
        StringWriter certStringWriter = new StringWriter();
        PemWriter pemWriter = new PemWriter(certStringWriter);
        pemWriter.writeObject(new PemObject("CERTIFICATE", certificate.getEncoded()));
        pemWriter.close();
        String certString = certStringWriter.toString();
        return certString;
    }

    public static boolean validateCertificate(X509Certificate[] certs) throws CertificateEncodingException,
            IOException, CertInvalidException {
        logger.info("validating certificate...");
        X509Certificate certificate = null;
        boolean certVerified;
        try {
            certificate = certs[0];
        } catch (NullPointerException nullPointerException) {
            logger.info("Certificate is not provided.");
            throw new CertInvalidException("Certificate is not provided");
        }
        String commonName = CertX509Handler.getDomainName(certificate);
        logger.info("Common Name :" + commonName);
        logger.info(CertX509Handler.stringFromCert(certificate));    //should remove
        certVerified = MSchainCertHandler.validateCertificate(commonName, certificate);
        return certVerified;
    }

    public static X509Certificate loadCertFromKeyStore() throws IOException, KeyStoreException, CertificateException, NoSuchAlgorithmException {
        logger.info("loading certs from keystore...");
        KeyStore keyStore = KeyStore.getInstance("JKS");
        InputStream is = new FileInputStream(new File(Config.SOURCE_PATH + Config.getKeyStoreName()));
        keyStore.load(is, Config.getKeyStorePassword().toCharArray());
        X509Certificate certificate = (X509Certificate) keyStore.getCertificate(Config.ALIAS_NAME);
        return certificate;
    }

    public static X509Certificate getHeaderCert(HttpHeaders httpHeaders) throws IOException, CertificateException {
        List<String> certStrings = httpHeaders.get("Cert");
        String server_cert = certStrings.get(0).replace(" ","\n")
                .replaceAll("-----BEGIN\nCERTIFICATE-----","-----BEGIN CERTIFICATE-----")
                .replaceAll("-----END\nCERTIFICATE-----","-----END CERTIFICATE-----");

        System.out.println(server_cert);
        X509Certificate serverCert = KeyOperator.readCertificate(server_cert);
        return serverCert;
    }

}
