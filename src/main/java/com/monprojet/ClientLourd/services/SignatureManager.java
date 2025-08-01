package com.monprojet.ClientLourd.services;

import org.bouncycastle.asn1.x500.X500Name;
import org.bouncycastle.cert.X509CertificateHolder;
import org.bouncycastle.cert.jcajce.JcaX509CertificateConverter;
import org.bouncycastle.cms.*;
import org.bouncycastle.cms.jcajce.JcaSimpleSignerInfoVerifierBuilder;
import org.bouncycastle.jce.provider.BouncyCastleProvider;
import org.bouncycastle.operator.OperatorCreationException;
import org.bouncycastle.util.Store;

import java.io.File;
import java.io.FileInputStream;
import java.security.Security;
import java.security.cert.X509Certificate;
import java.util.*;

public class SignatureManager {
    static {
        Security.addProvider(new BouncyCastleProvider());
    }

    public static boolean verifyDocumentSignature(File document, File p7sFile) 
        throws Exception {
        
        // 1. Load PKCS#7 structure
        CMSSignedData signedData = new CMSSignedData(
            new FileInputStream(p7sFile));

        // 2. Verify all signers
        return verifyAllSigners(document, signedData);
    }

    private static boolean verifyAllSigners(File document, CMSSignedData signedData) 
        throws Exception {
        
        Store<X509CertificateHolder> certStore = signedData.getCertificates();
        Collection<SignerInformation> signers = signedData.getSignerInfos().getSigners();
        
        if (signers.isEmpty()) {
            throw new CMSException("No signatures found");
        }

        // 3. Process each signer
        for (SignerInformation signer : signers) {
            if (!verifySigner(document, signer, certStore)) {
                return false;
            }
        }
        return true;
    }

    private static boolean verifySigner(File document, 
            SignerInformation signer, Store<X509CertificateHolder> certStore) 
            throws Exception {
        
        // 4. Get signer's certificate
        X509CertificateHolder certHolder = getCertificateForSigner(signer, certStore);
        X509Certificate cert = new JcaX509CertificateConverter()
            .setProvider("BC")
            .getCertificate(certHolder);

        // 5. Validate certificate
        validateCertificate(cert);

        // 6. Verify content
        try (FileInputStream contentStream = new FileInputStream(document)) {
            SignerInformationVerifier verifier = new JcaSimpleSignerInfoVerifierBuilder()
                .setProvider("BC")
                .build(cert.getPublicKey());

            return signer.verify(verifier);
        }
    }

    private static X509CertificateHolder getCertificateForSigner(
            SignerInformation signer, Store<X509CertificateHolder> certStore) 
            throws CMSException {
        
        Collection<X509CertificateHolder> certs = certStore.getMatches(signer.getSID());
        if (certs.isEmpty()) {
            throw new CMSException("No certificate found for signer: " 
                + signer.getSID().getSerialNumber());
        }
        return certs.iterator().next();
    }

    private static void validateCertificate(X509Certificate cert) 
        throws Exception {
        
        cert.checkValidity();
        
        // Example: Check issuer
      /*  X500Name issuer = new X500Name(cert.getIssuerX500Principal().getName());
        if (!issuer.equals(new X500Name("CN=My Trusted CA,O=My Org"))) {
            throw new SecurityException("Untrusted certificate issuer");
        }*/
    }
}