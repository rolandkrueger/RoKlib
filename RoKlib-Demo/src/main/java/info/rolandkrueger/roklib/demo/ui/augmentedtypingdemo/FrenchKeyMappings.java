package info.rolandkrueger.roklib.demo.ui.augmentedtypingdemo;

import info.rolandkrueger.roklib.ui.swing.augmentedtyping.AugmentedTypingKeyMapping;

public class FrenchKeyMappings extends AugmentedTypingKeyMapping
{
  public FrenchKeyMappings ()
  {
    addMapping ('A', new Character[] {'\u00C0', '\u00C2', '\u00C6'});
    addMapping ('a', new Character[] {'\u00E0', '\u00E2', '\u00E6'});
    addMapping ('C', new Character[] {'\u00C7'});
    addMapping ('c', new Character[] {'\u00E7'});
    addMapping ('E', new Character[] {'\u00C8', '\u00C9', '\u00CA', '\u00CB'});
    addMapping ('e', new Character[] {'\u00E8', '\u00E9', '\u00EA', '\u00EB'});
    addMapping ('I', new Character[] {'\u00CE', '\u00CF'});
    addMapping ('i', new Character[] {'\u00EE', '\u00EF'});
    addMapping ('O', new Character[] {'\u00D4', '\u0152'});
    addMapping ('o', new Character[] {'\u00F4', '\u0153'});
    addMapping ('U', new Character[] {'\u00D9', '\u00DB'});
    addMapping ('u', new Character[] {'\u00F9', '\u00FB'});
    addMapping ('Y', new Character[] {'\u0178'});
    addMapping ('y', new Character[] {'\u00FF'});
    addMapping ('>', new Character[] {'\u00BB'});
    addMapping ('<', new Character[] {'\u00AB'});
  }
}
