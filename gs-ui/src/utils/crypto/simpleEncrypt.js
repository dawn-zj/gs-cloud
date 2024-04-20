
export function caesar(cipherText, key) {
  let es = ''
  for (let i = 0; i < cipherText.length; i++) {
    // 是小写字母
    if (cipherText.charCodeAt(i) >= 65 && cipherText.charCodeAt(i) <= 90) {
      es += String.fromCharCode((cipherText.charCodeAt(i) - 65 + key + 26) % 26 + 65)
    } else if (cipherText.charCodeAt(i) >= 97 && cipherText.charCodeAt(i) <= 122) {
      es += String.fromCharCode((cipherText.charCodeAt(i) - 97 + key + 26) % 26 + 97)
    } else {
      // 特殊符号不做处理
      es += String.fromCharCode(cipherText.charCodeAt(i))
    }
  }
  return es
}
