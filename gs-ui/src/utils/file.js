// 把图片转成base64编码
export function getBase64(file) {
  return new Promise(function (resolve, reject) {
    let reader = new FileReader();
    let imgResult = "";
    reader.readAsDataURL(file);
    reader.onload = function (e) {
      imgResult = e.target.result; // 这种写法可兼容移动端
    };
    reader.onerror = function (error) {
      reject(error);
    };
    reader.onloadend = function () {
      let item = ";base64,";
      imgResult = imgResult.substring(imgResult.indexOf(item) + item.length);
      resolve(imgResult);
    }
  })
}

// 获取文件后缀
export function getFileType(fileName) {
  let spotIndex = fileName.lastIndexOf(".");
  let fileLength = fileName.length;
  let fileType = fileName.substring(spotIndex, fileLength);
  return fileType;
}

// 下载文件
export function download(str, fileName) {
  let binaryData = [];
  binaryData.push(str);
  var a = document.createElement('a')
  var url = window.URL.createObjectURL(new Blob(binaryData))
  a.href = url
  a.download = fileName // 文件名
  a.click()

}

// 下载文件，base64图片下载
export function downloadBase64(base64, fileName) {
  var blob = base642blob(base64)
  download(blob, fileName)
}

export function base642blob(base64) {
  var byteCharacters = atob(base64);
  var byteNumbers = new Array(byteCharacters.length);
  for (var i = 0; i < byteCharacters.length; i++) {
    byteNumbers[i] = byteCharacters.charCodeAt(i);
  }
  var byteArray = new Uint8Array(byteNumbers);
  var blob = new Blob([byteArray], {
    type: undefined,
  });
  return blob;
}
