/* File taken from https://gist.github.com/mythosil/1292283 */
#include <stdio.h>
#include <stdlib.h>
#include <string.h>
#include <openssl/evp.h>

int main(int argc, const char* argv[])
{
  if (argc <= 3) {
    fprintf(stderr, "Usage: %s <key> <iv> <data>\n", argv[0]);
    exit(EXIT_FAILURE);
  }

  const char *key, *iv, *data;
  int key_length, iv_length, data_length;
  key = argv[1];
  key_length = strlen(key);
  iv = argv[2];
  iv_length = strlen(iv);
  data = argv[3];
  data_length = strlen(data);

  const EVP_CIPHER *cipher;
  int cipher_key_length, cipher_iv_length;
  cipher = EVP_aes_128_cbc();
  cipher_key_length = EVP_CIPHER_key_length(cipher);
  cipher_iv_length = EVP_CIPHER_iv_length(cipher);

  if (key_length != cipher_key_length) {
    fprintf(stderr, "Error: key length must be %d\n", cipher_key_length);
    exit(EXIT_FAILURE);
  }
  if (iv_length != cipher_iv_length) {
    fprintf(stderr, "Error: iv length must be %d\n", cipher_iv_length);
    exit(EXIT_FAILURE);
  }

  EVP_CIPHER_CTX ctx;
  int i, cipher_length, final_length;
  unsigned char *ciphertext;

  EVP_CIPHER_CTX_init(&ctx);
  EVP_EncryptInit_ex(&ctx, cipher, NULL, (unsigned char *)key, (unsigned char *)iv);

  cipher_length = data_length + EVP_MAX_BLOCK_LENGTH;
  ciphertext = (unsigned char *)malloc(cipher_length);

  EVP_EncryptUpdate(&ctx, ciphertext, &cipher_length, (unsigned char *)data, data_length);
  EVP_EncryptFinal_ex(&ctx, ciphertext + cipher_length, &final_length);

  for (i = 0; i < cipher_length + final_length; i++)
    printf("%02x", ciphertext[i]);
  printf("\n");

  free(ciphertext);

  EVP_CIPHER_CTX_cleanup(&ctx);

  return 0;
}
