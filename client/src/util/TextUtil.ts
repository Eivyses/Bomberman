import { Text } from '../constant/Text';
import { Color } from './../constant/Color';

interface TextStyle {
  font: string;
  fill: string;
}

export class TextUtil {
  public static STANDARD_WHITE_TEXT: TextStyle = {
    font: Text.STANDARD,
    fill: Color.WHITE
  };

  public static HEADER_WHITE_TEXT: TextStyle = {
    font: Text.HEADER,
    fill: Color.WHITE
  };
}
