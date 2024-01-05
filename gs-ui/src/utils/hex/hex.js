export function strToHex(str) {
  var hexCharCode = [];
  var chars = ["0","1","2","3","4","5","6","7","8","9","A","B","C","D","E","F"];
  for(var i = 0; i < str.length; i++) {
    var bit = (str[i] & 0x0f0) >> 4;
    hexCharCode.push(chars[bit]);
    var bit = str[i] & 0x0f;
    hexCharCode.push(chars[bit]);
  }
  return hexCharCode.join("");
}

export function format(str) {
  var result = "";
  for(var i = 0; i < str.length; i++) {
    result += str[i];
    // 每两位加一个空格
    if ((i + 1) % 2 == 0) {
      result += " ";
    }
    // 每32位加一个回车
    if ((i + 1) % 32 == 0) {
      result += "\n";
    }
  }
  return result;
}
