# TinyCipher

A simple CLI tool to encrypt or decrypt files.



### Usage

```
java -jar <JAR_FILE> <ARGUMENTS>
```



### Arguments

Encrypt file:

```
encrypt [--base64] <input> <output>
      <input>    Path to input file
      <output>   Path to output file
      --base64   Encode output to Base64
```

Decrypt file:

```
decrypt [--base64] <input> <output>
      <input>    Path to input file
      <output>   Path to output file
      --base64   Decode input from Base64
```