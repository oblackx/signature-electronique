package com.monprojet.ClientLourd.services;


import org.bouncycastle.cert.jcajce.JcaCertStore;
import org.bouncycastle.operator.jcajce.JcaDigestCalculatorProviderBuilder;
import org.bouncycastle.asn1.cms.CMSObjectIdentifiers;
import org.bouncycastle.cert.X509CertificateHolder;
import org.bouncycastle.cert.jcajce.JcaX509CertificateConverter;
import org.bouncycastle.cms.*;
import org.bouncycastle.cms.jcajce.JcaSignerInfoGeneratorBuilder;
import org.bouncycastle.cms.jcajce.JcaSimpleSignerInfoVerifierBuilder;
import org.bouncycastle.jce.provider.BouncyCastleProvider;
import org.bouncycastle.operator.ContentSigner;
import org.bouncycastle.operator.OperatorCreationException;
import org.bouncycastle.operator.jcajce.JcaContentSignerBuilder;
import org.bouncycastle.util.Store;

import java.io.*;
import java.security.*;
import java.security.cert.X509Certificate;
import java.util.*;

public class SignatureManager {
    static {
        Security.addProvider(new BouncyCastleProvider());
    }

    // 🔐 ========== SIGNATURE DETACHÉE ==========
    public static void signDocumentDetached(File documentToSign, File p7sOutputFile,
                                            PrivateKey privateKey, X509Certificate certificate)
            throws Exception {

        // Crée une structure CMS détachée
        CMSTypedData cmsData = new CMSProcessableFile(documentToSign);
        List<X509Certificate> certList = Collections.singletonList(certificate);
        Store certs = new JcaCertStore(certList);

        // Création du Signer
        ContentSigner contentSigner = new JcaContentSignerBuilder("SHA256withRSA")
                .setProvider("BC")
                .build(privateKey);

        SignerInfoGenerator signerInfoGenerator = new JcaSignerInfoGeneratorBuilder(
                new JcaDigestCalculatorProviderBuilder().setProvider("BC").build())
                .build(contentSigner, certificate);

        CMSSignedDataGenerator generator = new CMSSignedDataGenerator();
        generator.addSignerInfoGenerator(signerInfoGenerator);
        generator.addCertificates(certs);

        // Génère la signature (detachée)
        CMSSignedData signedData = generator.generate(cmsData, false); // false = detached

        // Écrit dans le fichier .p7s
        try (FileOutputStream fos = new FileOutputStream(p7sOutputFile)) {
            fos.write(signedData.getEncoded());
        }
    }

    // ✅ ========== VÉRIFICATION ==========
    public static boolean verifyDocumentSignature(File document, File p7sFile)
            throws Exception {

        // 1. Load PKCS#7 structure
        CMSSignedData signedData = new CMSSignedData(
                new CMSProcessableFile(document),
                new FileInputStream(p7sFile)
        );

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
                                        SignerInformation signer,
                                        Store<X509CertificateHolder> certStore)
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

    private static void validateCertificate(X509Certificate cert) throws Exception {
        cert.checkValidity(); // Vérifie seulement la date de validité
        // ❌ Désactivé pour l’instant : vérification CN/issuer
    }
    public static void signDocumentDetached(File fichier, File certificatP12, String password, File outputP7s)
            throws Exception {
        KeyStore keystore = KeyStore.getInstance("PKCS12", "BC");
        try (FileInputStream fis = new FileInputStream(certificatP12)) {
            keystore.load(fis, password.toCharArray());
        }

        String alias = keystore.aliases().nextElement();
        PrivateKey privateKey = (PrivateKey) keystore.getKey(alias, password.toCharArray());
        java.security.cert.Certificate cert = keystore.getCertificate(alias);

        X509Certificate x509Cert = (X509Certificate) cert;

        List<X509Certificate> certList = new ArrayList<>();
        certList.add(x509Cert);
        Store<?> certs = new JcaCertStore(certList);

        CMSSignedDataGenerator gen = new CMSSignedDataGenerator();
        ContentSigner signer = new JcaContentSignerBuilder("SHA256withRSA")
                .setProvider("BC")
                .build(privateKey);
        gen.addSignerInfoGenerator(new JcaSignerInfoGeneratorBuilder(
                new JcaDigestCalculatorProviderBuilder().setProvider("BC").build())
                .build(signer, x509Cert));
        gen.addCertificates(certs);

        CMSProcessableByteArray data = new CMSProcessableByteArray(java.nio.file.Files.readAllBytes(fichier.toPath()));
        CMSSignedData signedData = gen.generate(data, false); // false = detached

        try (FileOutputStream fos = new FileOutputStream(outputP7s)) {
            fos.write(signedData.getEncoded());
        }
    }

}
    