package com.frontline.mainservice.utils;

import org.bouncycastle.asn1.x500.RDN;
import org.bouncycastle.asn1.x500.X500Name;
import org.bouncycastle.asn1.x500.style.BCStyle;
import org.bouncycastle.cert.jcajce.JcaX509CertificateHolder;
import org.bouncycastle.util.io.pem.PemObject;
import org.bouncycastle.util.io.pem.PemWriter;

import java.io.IOException;
import java.io.StringWriter;
import java.security.cert.CertificateEncodingException;
import java.security.cert.X509Certificate;
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
        return certString.substring(0, certString.length() - 1);
    }

    public static boolean validateCertificate(X509Certificate[] certs) throws CertificateEncodingException,
            IOException, CertInvalidException {
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

}
